package de.benjaminborbe.task.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.dao.TaskBean;
import de.benjaminborbe.task.dao.TaskContextBean;
import de.benjaminborbe.task.dao.TaskContextDao;
import de.benjaminborbe.task.dao.TaskContextManyToManyRelation;
import de.benjaminborbe.task.dao.TaskDao;
import de.benjaminborbe.task.tools.TaskComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ThreadPoolExecuter;
import de.benjaminborbe.tools.util.ThreadPoolExecuterBuilder;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class TaskServiceImpl implements TaskService {

	private static final int FILTER_THREADS = 20;

	private static final int DURATION_WARN = 150;

	private final class FilterRunnable implements Runnable {

		private final Collection<TaskContextIdentifier> taskContextIdentifiers;

		private final ThreadResult<Task> threadResult;

		private final Task task;

		private FilterRunnable(final Collection<TaskContextIdentifier> taskContextIdentifiers, final ThreadResult<Task> threadResult, final Task task) {
			this.taskContextIdentifiers = taskContextIdentifiers;
			this.threadResult = threadResult;
			this.task = task;
		}

		@Override
		public void run() {
			try {
				if (taskContextIdentifiers.size() == 0) {
					final StorageIterator a = taskContextManyToManyRelation.getA(task.getId());
					if (!a.hasNext()) {
						threadResult.set(task);
					}
				}
				else {
					boolean match = false;
					for (final TaskContextIdentifier taskContextIdentifier : taskContextIdentifiers) {
						if (!match && taskContextManyToManyRelation.exists(task.getId(), taskContextIdentifier)) {
							match = true;
						}
					}
					if (match) {
						threadResult.set(task);
					}
				}
			}
			catch (final Exception e) {
				logger.debug(e.getClass().getName(), e);
			}
		}
	}

	private final class TaskMatchImpl implements TaskMatch {

		private final BeanMatch<Task> match;

		private TaskMatchImpl(final BeanMatch<Task> match) {
			this.match = match;
		}

		@Override
		public Task getTask() {
			return match.getBean();
		}

		@Override
		public int getMatchCounter() {
			return match.getMatchCounter();
		}
	}

	private final class TaskSearcher extends BeanSearcher<Task> {

		private static final String URL = "url";

		private static final String NAME = "content";

		private static final String DESCRIPTION = "title";

		@Override
		protected Map<String, String> getSearchValues(final Task bean) {
			final Map<String, String> values = new HashMap<String, String>();
			values.put(DESCRIPTION, bean.getDescription());
			values.put(NAME, bean.getName());
			values.put(URL, bean.getUrl());
			return values;
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			final Map<String, Integer> values = new HashMap<String, Integer>();
			values.put(DESCRIPTION, 1);
			values.put(NAME, 2);
			values.put(URL, 1);
			return values;
		}
	}

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final CalendarUtil calendarUtil;

	private final IdGeneratorUUID idGeneratorUUID;

	private final Logger logger;

	private final TaskContextDao taskContextDao;

	private final TaskContextManyToManyRelation taskContextManyToManyRelation;

	private final TaskDao taskDao;

	private final ValidationExecutor validationExecutor;

	private final DurationUtil durationUtil;

	private final TaskComparator taskComparator;

	private final ThreadPoolExecuterBuilder threadPoolExecuterBuilder;

	@Inject
	public TaskServiceImpl(
			final Logger logger,
			final TaskDao taskDao,
			final TaskComparator taskComparator,
			final IdGeneratorUUID idGeneratorUUID,
			final TaskContextDao taskContextDao,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final ValidationExecutor validationExecutor,
			final TaskContextManyToManyRelation taskContextManyToManyRelation,
			final CalendarUtil calendarUtil,
			final DurationUtil durationUtil,
			final ThreadPoolExecuterBuilder threadPoolExecuterBuilder) {
		this.logger = logger;
		this.taskDao = taskDao;
		this.taskComparator = taskComparator;
		this.idGeneratorUUID = idGeneratorUUID;
		this.taskContextDao = taskContextDao;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.validationExecutor = validationExecutor;
		this.taskContextManyToManyRelation = taskContextManyToManyRelation;
		this.calendarUtil = calendarUtil;
		this.durationUtil = durationUtil;
		this.threadPoolExecuterBuilder = threadPoolExecuterBuilder;
	}

	@Override
	public void addTaskContext(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier)
			throws TaskServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			expectOwner(sessionIdentifier, taskDao.load(taskIdentifier));
			expectOwner(sessionIdentifier, taskContextDao.load(taskContextIdentifier));

			logger.trace("addTaskContext");
			taskContextManyToManyRelation.add(taskIdentifier, taskContextIdentifier);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("completeTask: " + taskIdentifier + " started");

			final TaskBean task = taskDao.load(taskIdentifier);
			expectOwner(sessionIdentifier, task);

			// update child parent
			final EntityIterator<TaskBean> childIterator = taskDao.getTaskChilds(taskIdentifier);
			while (childIterator.hasNext()) {
				final TaskBean child = childIterator.next();
				if (!Boolean.TRUE.equals(child.getCompleted())) {
					throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not all childs are completed")));
				}
			}

			task.setCompletionDate(calendarUtil.now());
			task.setCompleted(true);
			taskDao.save(task);

			// repeat
			if (task.getRepeatDue() != null || task.getRepeatStart() != null) {
				logger.trace("completeTask: " + taskIdentifier + " create repeat");
				final Calendar due = calcRepeat(task.getRepeatDue());
				final Calendar start = calcRepeat(task.getRepeatStart());
				final List<TaskContextIdentifier> contexts = new ArrayList<TaskContextIdentifier>();
				final StorageIterator i = taskContextManyToManyRelation.getA(taskIdentifier);
				while (i.hasNext()) {
					contexts.add(createTaskContextIdentifier(i.next().getString()));
				}
				final TaskDto taskDto = new TaskDto(task, contexts);
				taskDto.setStart(start);
				taskDto.setDue(due);
				createTask(sessionIdentifier, taskDto);
			}
			logger.trace("completeTask: " + taskIdentifier + " finished");
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException,
			ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("createTaskContext");

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskContextIdentifier taskContextIdentifier = createTaskContextIdentifier(idGeneratorUUID.nextId());
			final TaskContextBean task = taskContextDao.create();
			task.setId(taskContextIdentifier);
			task.setName(name);
			task.setOwner(userIdentifier);

			final ValidationResult errors = validationExecutor.validate(task);
			if (errors.hasErrors()) {
				logger.warn("TaskContext " + errors.toString());
				throw new ValidationException(errors);
			}

			taskContextDao.save(task);
			return taskContextIdentifier;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public TaskContextIdentifier createTaskContextIdentifier(final String id) throws TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (id != null && id.length() > 0) {
				return new TaskContextIdentifier(id);
			}
			else {
				return null;
			}
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public TaskIdentifier createTaskIdentifier(final String id) throws TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {

			if (id != null && id.length() > 0) {
				return new TaskIdentifier(id);
			}
			else {
				return null;
			}
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteContextTask(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("deleteContextTask");
			final TaskContextBean taskContext = taskContextDao.load(taskContextIdentifier);
			expectOwner(sessionIdentifier, taskContext);
			taskContextDao.delete(taskContext);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("deleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			if (task == null) {
				logger.trace("task already deleted");
				return;
			}
			expectOwner(sessionIdentifier, task);

			// update child parent
			final EntityIterator<TaskBean> i = taskDao.getTaskChilds(taskIdentifier);
			while (i.hasNext()) {
				final TaskBean child = i.next();
				child.setParentId(task.getParentId());
				taskDao.save(child);
			}

			taskDao.delete(task);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Task getTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTask");
			final Task task = taskDao.load(taskIdentifier);
			if (task == null) {
				logger.info("task not found with id " + taskIdentifier);
				return null;
			}
			expectOwner(sessionIdentifier, task);
			return task;
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}

		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public List<TaskContext> getTaskContexts(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectOwner(sessionIdentifier, taskDao.load(taskIdentifier));

			logger.trace("getTaskContexts for task: " + taskIdentifier);
			final StorageIterator i = taskContextManyToManyRelation.getA(taskIdentifier);
			final List<TaskContext> result = new ArrayList<TaskContext>();
			while (i.hasNext()) {
				final String id = i.next().getString();
				logger.trace("add taskcontext: " + id);
				final TaskContextBean taskContextBean = taskContextDao.load(createTaskContextIdentifier(id));
				if (taskContextBean != null) {
					result.add(taskContextBean);
				}
			}
			logger.trace("found " + result.size() + " contexts");
			return result;
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public List<TaskContext> getTasksContexts(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final List<TaskContextBean> taskContexts = taskContextDao.getAllByUser(userIdentifier);
			final List<TaskContext> result = new ArrayList<TaskContext>();
			for (final TaskContextBean taskContext : taskContexts) {
				result.add(taskContext);
			}
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void replaceTaskContext(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			TaskBean task = taskDao.load(taskIdentifier);
			expectOwner(sessionIdentifier, task);
			for (final TaskContextIdentifier taskContextIdentifier : taskContextIdentifiers) {
				expectOwner(sessionIdentifier, taskContextDao.load(taskContextIdentifier));
			}

			while (task.getParentId() != null) {
				task = taskDao.load(task.getParentId());
			}
			replaceTaskContext(task, taskContextIdentifiers);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void replaceTaskContext(final Task task, final Collection<TaskContextIdentifier> taskContextIdentifiers) throws StorageException, EntityIteratorException {
		logger.trace("addTaskContext");
		taskContextManyToManyRelation.removeA(task.getId());
		for (final TaskContextIdentifier taskContextIdentifier : taskContextIdentifiers) {
			taskContextManyToManyRelation.add(task.getId(), taskContextIdentifier);
		}

		final EntityIterator<TaskBean> i = taskDao.getTaskChilds(task.getId());
		while (i.hasNext()) {
			final Task taskChild = i.next();
			replaceTaskContext(taskChild, taskContextIdentifiers);
		}
	}

	private void expectOwner(final SessionIdentifier sessionIdentifier, final Task task) throws PermissionDeniedException, LoginRequiredException, TaskServiceException,
			AuthorizationServiceException {
		authorizationService.expectUser(sessionIdentifier, task.getOwner());
	}

	private void expectOwner(final SessionIdentifier sessionIdentifier, final TaskContext taskContext) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException, AuthorizationServiceException {
		authorizationService.expectUser(sessionIdentifier, taskContext.getOwner());
	}

	@Override
	public void swapPrio(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifierA, final TaskIdentifier taskIdentifierB) throws PermissionDeniedException,
			LoginRequiredException, TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final TaskBean taskA = taskDao.load(taskIdentifierA);
			authorizationService.expectUser(sessionIdentifier, taskA.getOwner());
			final TaskBean taskB = taskDao.load(taskIdentifierB);
			authorizationService.expectUser(sessionIdentifier, taskB.getOwner());

			logger.trace("swapPrio " + taskIdentifierA + " <=> " + taskIdentifierB);
			int p1 = taskA.getPriority() != null ? taskA.getPriority() : 0;
			final int p2 = taskB.getPriority() != null ? taskB.getPriority() : 0;
			if (p2 == p1) {
				p1++;
			}
			taskA.setPriority(p2);
			taskB.setPriority(p1);
			taskDao.save(taskA);
			taskDao.save(taskB);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void uncompleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("unCompleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			expectOwner(sessionIdentifier, task);

			task.setCompletionDate(null);
			task.setCompleted(false);
			taskDao.save(task);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}

		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateTask(final SessionIdentifier sessionIdentifier, final TaskDto taskDto) throws TaskServiceException, PermissionDeniedException, LoginRequiredException,
			ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			if (taskDto.getId().equals(taskDto.getParentId())) {
				throw new TaskServiceException("taskId = parentId");
			}

			logger.trace("createTask");

			// check parent
			final TaskBean parentTask;
			if (taskDto.getParentId() != null) {
				parentTask = taskDao.load(taskDto.getParentId());
				authorizationService.expectUser(sessionIdentifier, parentTask.getOwner());
			}
			else {
				parentTask = null;
			}
			final TaskBean task = taskDao.load(taskDto.getId());
			authorizationService.expectUser(sessionIdentifier, task.getOwner());

			task.setName(taskDto.getName());
			task.setDescription(taskDto.getDescription());
			task.setRepeatDue(taskDto.getRepeatDue());
			task.setRepeatStart(taskDto.getRepeatStart());
			task.setUrl(taskDto.getUrl());
			task.setParentId(taskDto.getParentId());
			task.setStart(taskDto.getStart());
			task.setDue(taskDto.getDue());

			updateTaskStartDueChildAndSave(parentTask, task);

			// only update if set
			if (taskDto.getContexts() != null) {
				replaceTaskContext(sessionIdentifier, taskDto.getId(), taskDto.getContexts());
			}

		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void updateTaskStartDueChildAndSave(final TaskBean parentTask, final TaskBean task) throws StorageException, EntityIteratorException, ValidationException {
		if (parentTask != null) {
			task.setStart(calendarUtil.max(task.getStart(), parentTask.getStart()));
			task.setDue(calendarUtil.min(task.getDue(), parentTask.getDue()));
		}

		final ValidationResult errors = validationExecutor.validate(task);
		if (errors.hasErrors()) {
			logger.warn("Task " + errors.toString());
			throw new ValidationException(errors);
		}

		taskDao.save(task);

		// update childs
		final EntityIterator<TaskBean> i = taskDao.getTaskChilds(task.getId());
		while (i.hasNext()) {
			final TaskBean child = i.next();
			updateTaskStartDueChildAndSave(task, child);
		}
	}

	@Override
	public List<Task> getTaskChilds(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectOwner(sessionIdentifier, taskDao.load(taskIdentifier));

			logger.trace("getTaskChilds");
			final List<Task> result = new ArrayList<Task>();
			final EntityIterator<TaskBean> i = taskDao.getTaskChilds(taskIdentifier);
			while (i.hasNext()) {
				final Task task = i.next();
				result.add(task);
			}
			logger.trace("getTasksCompleted found " + result.size());
			Collections.sort(result, taskComparator);
			return result;
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}

		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTasksNotCompleted");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.trace("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			final EntityIterator<TaskBean> i = taskDao.getTasksNotCompleted(userIdentifier);
			while (i.hasNext()) {
				final Task task = i.next();
				result.add(task);
			}
			Collections.sort(result, taskComparator);
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTasksCompleted");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.trace("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			final EntityIterator<TaskBean> i = taskDao.getTasksCompleted(userIdentifier);
			while (i.hasNext()) {
				final Task task = i.next();
				result.add(task);
			}
			Collections.sort(result, taskComparator);
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier, final Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			return filterWithContexts(getTasksCompleted(sessionIdentifier), taskContextIdentifiers);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private List<Task> filterWithContexts(final List<Task> tasks, final Collection<TaskContextIdentifier> taskContextIdentifiers) throws StorageException {
		logger.trace("filterWithContexts: " + tasks.size());

		final List<ThreadResult<Task>> threadResults = new ArrayList<ThreadResult<Task>>();

		final ThreadPoolExecuter threadPoolExecuter = threadPoolExecuterBuilder.build("filter task by context", FILTER_THREADS);
		for (final Task task : tasks) {
			final ThreadResult<Task> threadResult = new ThreadResult<Task>();
			threadResults.add(threadResult);
			threadPoolExecuter.execute(new FilterRunnable(taskContextIdentifiers, threadResult, task));
		}

		try {
			threadPoolExecuter.shutDown();
			threadPoolExecuter.awaitTermination(1, TimeUnit.SECONDS);
		}
		catch (final InterruptedException e) {
		}

		final List<Task> result = new ArrayList<Task>();
		for (final ThreadResult<Task> threadResult : threadResults) {
			final Task task = threadResult.get();
			if (task != null) {
				result.add(task);
			}
		}
		logger.trace("filterWithContexts: " + tasks.size() + " => " + result.size());
		return result;
	}

	@Override
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {

			return filterWithContexts(getTasksNotCompleted(sessionIdentifier), taskContextIdentifiers);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateTaskContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final String name) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.trace("updateTaskContext");

			final TaskContextBean taskContext = taskContextDao.load(taskContextIdentifier);
			authorizationService.expectUser(sessionIdentifier, taskContext.getOwner());

			taskContext.setName(name);

			final ValidationResult errors = validationExecutor.validate(taskContext);
			if (errors.hasErrors()) {
				logger.warn("TaskContext " + errors.toString());
				throw new ValidationException(errors);
			}
			taskContextDao.save(taskContext);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private Calendar calcRepeat(final Long repeat) {
		if (repeat != null && repeat > 0) {
			return calendarUtil.addDays(calendarUtil.today(), repeat);
		}
		else {
			return null;
		}
	}

	@Override
	public TaskContext getTaskContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTaskContext");
			final TaskContext taskContext = taskContextDao.load(taskContextIdentifier);
			if (taskContext == null) {
				logger.info("taskContext not found with id " + taskContextIdentifier);
				return null;
			}
			authorizationService.expectUser(sessionIdentifier, taskContext.getOwner());
			return taskContext;
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public List<TaskMatch> searchTasks(final SessionIdentifier sessionIdentifier, final int limit, final List<String> words) throws TaskServiceException, LoginRequiredException {
		final List<Task> beans = getTasksNotCompleted(sessionIdentifier);
		final BeanSearcher<Task> beanSearch = new TaskSearcher();
		final List<BeanMatch<Task>> matches = beanSearch.search(beans, limit, words);
		final List<TaskMatch> result = new ArrayList<TaskMatch>();
		for (final BeanMatch<Task> match : matches) {
			result.add(new TaskMatchImpl(match));
		}
		return result;
	}

	@Override
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final TaskDto taskDto) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("createTask");

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskBean parentTask;
			final Integer prio;
			// check parent
			if (taskDto.getParentId() != null) {
				parentTask = taskDao.load(taskDto.getParentId());
				prio = parentTask.getPriority();
				authorizationService.expectUser(sessionIdentifier, parentTask.getOwner());
			}
			else {
				prio = taskDao.getMaxPriority(userIdentifier) + 1;
				parentTask = null;
			}

			final TaskIdentifier taskIdentifier = createTaskIdentifier(idGeneratorUUID.nextId());
			final TaskBean taskBean = taskDao.create();

			taskBean.setId(taskIdentifier);
			taskBean.setName(taskDto.getName());
			taskBean.setDescription(taskDto.getDescription());
			taskBean.setOwner(userIdentifier);
			taskBean.setCompleted(false);
			taskBean.setRepeatDue(taskDto.getRepeatDue());
			taskBean.setRepeatStart(taskDto.getRepeatStart());
			taskBean.setUrl(taskDto.getUrl());
			taskBean.setPriority(prio);
			taskBean.setParentId(taskDto.getParentId());
			taskBean.setStart(taskDto.getStart());
			taskBean.setDue(taskDto.getDue());

			updateTaskStartDueChildAndSave(parentTask, taskBean);

			// only update if set
			if (taskDto.getContexts() != null) {
				replaceTaskContext(sessionIdentifier, taskIdentifier, taskDto.getContexts());
			}

			return taskIdentifier;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public TaskContext getTaskContextByName(final SessionIdentifier sessionIdentifier, final String taskContextName) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTaskContextByName");
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskContext taskContext = taskContextDao.findByName(userIdentifier, taskContextName);
			if (taskContext == null) {
				logger.info("taskContext not found with name " + taskContextName);
				return null;
			}
			expectOwner(sessionIdentifier, taskContext);
			return taskContext;
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}

		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
