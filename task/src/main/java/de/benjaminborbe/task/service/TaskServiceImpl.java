package de.benjaminborbe.task.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.dao.TaskBean;
import de.benjaminborbe.task.dao.TaskContextBean;
import de.benjaminborbe.task.dao.TaskContextDao;
import de.benjaminborbe.task.dao.TaskContextManyToManyRelation;
import de.benjaminborbe.task.dao.TaskDao;
import de.benjaminborbe.task.util.TaskNameComparator;
import de.benjaminborbe.task.util.TaskPrioComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.util.ComparatorChain;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class TaskServiceImpl implements TaskService {

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

	@Inject
	public TaskServiceImpl(
			final Logger logger,
			final TaskDao taskDao,
			final IdGeneratorUUID idGeneratorUUID,
			final TaskContextDao taskContextDao,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final ValidationExecutor validationExecutor,
			final TaskContextManyToManyRelation taskContextManyToManyRelation,
			final CalendarUtil calendarUtil,
			final DurationUtil durationUtil) {
		this.logger = logger;
		this.taskDao = taskDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.taskContextDao = taskContextDao;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.validationExecutor = validationExecutor;
		this.taskContextManyToManyRelation = taskContextManyToManyRelation;
		this.calendarUtil = calendarUtil;
		this.durationUtil = durationUtil;
	}

	@Override
	public void addTaskContext(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier)
			throws TaskServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			final TaskContextBean taskContext = taskContextDao.load(taskContextIdentifier);
			authorizationService.expectUser(sessionIdentifier, taskContext.getOwner());

			logger.trace("addTaskContext");
			taskContextManyToManyRelation.add(taskIdentifier, taskContextIdentifier);
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
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("completeTask: " + taskIdentifier + " started");
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());

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
					contexts.add(createTaskContextIdentifier(i.nextString()));
				}
				createTask(sessionIdentifier, task.getName(), task.getDescription(), task.getUrl(), task.getParentId(), start, due, task.getRepeatStart(), task.getRepeatDue(), contexts);
			}
			logger.trace("completeTask: " + taskIdentifier + " finished");
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new TaskServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final String name, final String description, final String url,
			final TaskIdentifier taskParentIdentifier, final Calendar start, final Calendar due, final Long repeatStart, final Long repeatDue,
			final Collection<TaskContextIdentifier> contexts) throws TaskServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("createTask");

			final TaskBean parentTask;
			// check parent
			if (taskParentIdentifier != null) {
				parentTask = taskDao.load(taskParentIdentifier);
				authorizationService.expectUser(sessionIdentifier, parentTask.getOwner());
			}
			else {
				parentTask = null;
			}

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskIdentifier taskIdentifier = createTaskIdentifier(idGeneratorUUID.nextId());
			final TaskBean task = taskDao.create();

			task.setId(taskIdentifier);
			task.setName(name);
			task.setDescription(description);
			task.setOwner(userIdentifier);
			task.setCompleted(false);
			task.setRepeatDue(repeatDue);
			task.setRepeatStart(repeatStart);
			task.setUrl(url);
			task.setPriority(taskDao.getMaxPriority(userIdentifier) + 1);
			task.setParentId(taskParentIdentifier);
			task.setStart(start);
			task.setDue(due);

			final ValidationResult errors = validationExecutor.validate(task);
			if (errors.hasErrors()) {
				logger.warn("Task " + errors.toString());
				throw new ValidationException(errors);
			}
			updateTaskStartDueChildAndSave(parentTask, task);

			// only update if set
			if (contexts != null) {
				for (final TaskContextIdentifier taskContextIdentifier : contexts) {
					replaceTaskContext(sessionIdentifier, taskIdentifier, taskContextIdentifier);
				}
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
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException {
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
			logger.trace("duration " + duration.getTime());
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
			logger.trace("duration " + duration.getTime());
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
			logger.trace("duration " + duration.getTime());
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
			authorizationService.expectUser(sessionIdentifier, taskContext.getOwner());
			taskContextDao.delete(taskContext);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
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
			authorizationService.expectUser(sessionIdentifier, task.getOwner());

			// update child parent
			final EntityIterator<TaskBean> i = taskDao.getTaskChilds(taskIdentifier);
			while (i.hasNext()) {
				final TaskBean child = i.next();
				child.setParentId(task.getParentId());
				taskDao.save(child);
			}

			taskDao.delete(task);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
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
		finally {
			logger.trace("duration " + duration.getTime());
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
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			return task;
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
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public List<TaskContext> getTaskContexts(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTaskContexts for task: " + taskIdentifier);
			final StorageIterator i = taskContextManyToManyRelation.getA(taskIdentifier);
			final List<TaskContext> result = new ArrayList<TaskContext>();
			while (i.hasNext()) {
				final String id = i.nextString();
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
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
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
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void replaceTaskContext(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier)
			throws TaskServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("addTaskContext");
			taskContextManyToManyRelation.removeA(taskIdentifier);
			taskContextManyToManyRelation.add(taskIdentifier, taskContextIdentifier);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
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
			logger.trace("duration " + duration.getTime());
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
			authorizationService.expectUser(sessionIdentifier, task.getOwner());

			task.setCompletionDate(null);
			task.setCompleted(false);
			taskDao.save(task);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void updateTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final String name, final String description, final String url,
			final TaskIdentifier taskParentIdentifier, final Calendar start, final Calendar due, final Long repeatStart, final Long repeatDue,
			final Collection<TaskContextIdentifier> contexts) throws TaskServiceException, PermissionDeniedException, LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			if (taskIdentifier.equals(taskParentIdentifier)) {
				throw new TaskServiceException("taskId = parentId");
			}

			logger.trace("createTask");

			// check parent
			final TaskBean parentTask;
			if (taskParentIdentifier != null) {
				parentTask = taskDao.load(taskParentIdentifier);
				authorizationService.expectUser(sessionIdentifier, parentTask.getOwner());
			}
			else {
				parentTask = null;
			}
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());

			task.setName(name);
			task.setDescription(description);
			task.setRepeatDue(repeatDue);
			task.setRepeatStart(repeatStart);
			task.setUrl(url);
			task.setParentId(taskParentIdentifier);
			task.setStart(start);
			task.setDue(due);

			final ValidationResult errors = validationExecutor.validate(task);
			if (errors.hasErrors()) {
				logger.warn("Task " + errors.toString());
				throw new ValidationException(errors);
			}
			updateTaskStartDueChildAndSave(parentTask, task);

			// only update if set
			if (contexts != null) {
				for (final TaskContextIdentifier taskContextIdentifier : contexts) {
					replaceTaskContext(sessionIdentifier, taskIdentifier, taskContextIdentifier);
				}
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
			logger.trace("duration " + duration.getTime());
		}
	}

	private void updateTaskStartDueChildAndSave(final TaskBean parentTask, final TaskBean task) throws StorageException, EntityIteratorException {
		if (parentTask != null) {
			task.setStart(calendarUtil.max(task.getStart(), parentTask.getStart()));
			task.setDue(calendarUtil.min(task.getDue(), parentTask.getDue()));
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
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.trace("getTaskChilds");
			final List<Task> result = new ArrayList<Task>();
			final EntityIterator<TaskBean> i = taskDao.getTaskChilds(taskIdentifier);
			while (i.hasNext()) {
				final Task task = i.next();
				result.add(task);
			}
			logger.trace("getTasksCompleted found " + result.size());
			return sort(result);
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
			logger.trace("duration " + duration.getTime());
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
			return sort(result);
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
			logger.trace("duration " + duration.getTime());
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
			return sort(result);
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
			logger.trace("duration " + duration.getTime());
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
			logger.trace("duration " + duration.getTime());
		}
	}

	private List<Task> filterWithContexts(final List<Task> tasks, final Collection<TaskContextIdentifier> taskContextIdentifiers) throws StorageException {
		logger.trace("filterWithContexts: " + tasks.size());
		final List<Task> result = new ArrayList<Task>();
		for (final Task task : tasks) {
			if (taskContextIdentifiers.size() == 0) {
				final StorageIterator a = taskContextManyToManyRelation.getA(task.getId());
				if (!a.hasNext()) {
					result.add(task);
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
					result.add(task);
				}
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
			logger.trace("duration " + duration.getTime());
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
			logger.trace("duration " + duration.getTime());
		}
	}

	protected List<Task> sort(final List<Task> result) {
		final List<Comparator<Task>> list = new ArrayList<Comparator<Task>>();
		list.add(new TaskPrioComparator());
		list.add(new TaskNameComparator());
		Collections.sort(result, new ComparatorChain<Task>(list));
		return result;
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
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public List<TaskMatch> searchTasks(final SessionIdentifier sessionIdentifier, final int limit, final String... words) throws TaskServiceException, LoginRequiredException {
		final List<Task> beans = getTasksNotCompleted(sessionIdentifier);
		final BeanSearcher<Task> beanSearch = new TaskSearcher();
		final List<BeanMatch<Task>> matches = beanSearch.search(beans, limit, words);
		final List<TaskMatch> result = new ArrayList<TaskMatch>();
		for (final BeanMatch<Task> match : matches) {
			result.add(new TaskMatchImpl(match));
		}
		return result;
	}
}
