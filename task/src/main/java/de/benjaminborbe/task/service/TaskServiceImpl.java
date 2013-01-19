package de.benjaminborbe.task.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.EntityIteratorFilter;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.dao.TaskBean;
import de.benjaminborbe.task.dao.TaskContextBean;
import de.benjaminborbe.task.dao.TaskContextDao;
import de.benjaminborbe.task.dao.TaskContextToUserManyToManyRelation;
import de.benjaminborbe.task.dao.TaskDao;
import de.benjaminborbe.task.util.TaskWithoutContextPredicate;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
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
		public int getMatchCounter() {
			return match.getMatchCounter();
		}

		@Override
		public Task getTask() {
			return match.getBean();
		}
	}

	private final class TaskSearcher extends BeanSearcher<Task> {

		private static final String DESCRIPTION = "title";

		private static final String NAME = "content";

		private static final String URL = "url";

		@Override
		protected Map<String, Integer> getSearchPrio() {
			final Map<String, Integer> values = new HashMap<String, Integer>();
			values.put(DESCRIPTION, 1);
			values.put(NAME, 2);
			values.put(URL, 1);
			return values;
		}

		@Override
		protected Map<String, String> getSearchValues(final Task bean) {
			final Map<String, String> values = new HashMap<String, String>();
			values.put(DESCRIPTION, bean.getDescription());
			values.put(NAME, bean.getName());
			values.put(URL, bean.getUrl());
			return values;
		}
	}

	private static final int DURATION_WARN = 300;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final CalendarUtil calendarUtil;

	private final DurationUtil durationUtil;

	private final IdGeneratorUUID idGeneratorUUID;

	private final Logger logger;

	private final TaskContextDao taskContextDao;

	private final TaskContextToUserManyToManyRelation taskContextToUserManyToManyRelation;

	private final TaskDao taskDao;

	private final ValidationExecutor validationExecutor;

	@Inject
	public TaskServiceImpl(
			final Logger logger,
			final TaskDao taskDao,
			final IdGeneratorUUID idGeneratorUUID,
			final TaskContextDao taskContextDao,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final ValidationExecutor validationExecutor,
			final TaskContextToUserManyToManyRelation taskContextToUserManyToManyRelation,
			final CalendarUtil calendarUtil,
			final DurationUtil durationUtil) {
		this.logger = logger;
		this.taskDao = taskDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.taskContextDao = taskContextDao;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.validationExecutor = validationExecutor;
		this.taskContextToUserManyToManyRelation = taskContextToUserManyToManyRelation;
		this.calendarUtil = calendarUtil;
		this.durationUtil = durationUtil;
	}

	@Override
	public void addUserToContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final UserIdentifier userIdentifier)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectOwner(sessionIdentifier, taskContextIdentifier);
			logger.debug("addUserToContext");

			if (!authenticationService.existsUser(userIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("user " + userIdentifier + " not exists")));
			}
			taskContextToUserManyToManyRelation.add(taskContextIdentifier, userIdentifier);
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

	private Calendar calcRepeat(final Long repeat) {
		if (repeat != null && repeat > 0) {
			return calendarUtil.addDays(calendarUtil.today(), repeat);
		}
		else {
			return null;
		}
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("completeTask: " + taskIdentifier + " started");

			final TaskBean task = taskDao.load(taskIdentifier);
			expectOwner(sessionIdentifier, task);

			if (Boolean.TRUE.equals(task.getCompleted())) {
				logger.info("already completed => skip");
				return;
			}

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
				logger.debug("completeTask: " + taskIdentifier + " create repeat");
				final Calendar due = calcRepeat(task.getRepeatDue());
				final Calendar start = calcRepeat(task.getRepeatStart());
				final TaskDto taskDto = new TaskDto(task);
				taskDto.setStart(start);
				taskDto.setDue(due);
				createTask(sessionIdentifier, taskDto);
			}
			logger.debug("completeTask: " + taskIdentifier + " finished");
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
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final TaskDto taskDto) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("createTask");

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
			final TaskBean task = taskDao.create();

			task.setId(taskIdentifier);
			task.setName(taskDto.getName());
			task.setDescription(taskDto.getDescription());
			task.setOwner(userIdentifier);
			task.setCompleted(false);
			task.setRepeatDue(taskDto.getRepeatDue());
			task.setRepeatStart(taskDto.getRepeatStart());
			task.setUrl(taskDto.getUrl());
			task.setPriority(prio);
			task.setParentId(taskDto.getParentId());
			task.setStart(taskDto.getStart());
			task.setDue(taskDto.getDue());
			task.setContext(taskDto.getContext());
			task.setFocus(taskDto.getFocus());

			saveTaskAndChilds(parentTask, task);

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
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException,
			ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("createTaskContext");

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

			logger.debug("deleteContextTask");
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

			logger.debug("deleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			if (task == null) {
				logger.debug("task already deleted");
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
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final Task task) throws PermissionDeniedException, LoginRequiredException, TaskServiceException {
		try {
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			if (currentUser == null) {
				throw new LoginRequiredException("login required");
			}
			if (currentUser.equals(task.getOwner())) {
				logger.debug("expectOwner => success");
				return;
			}
			if (task.getContext() != null) {
				final TaskContextBean context = taskContextDao.load(task.getContext());
				if (currentUser.equals(context.getOwner())) {
					logger.debug("expectOwner => success");
					return;
				}
				final StorageIterator userIterator = taskContextToUserManyToManyRelation.getA(task.getContext());
				while (userIterator.hasNext()) {
					final UserIdentifier user = new UserIdentifier(userIterator.next().getString());
					if (currentUser.equals(user)) {
						logger.debug("expectOwner => success");
						return;
					}
				}
			}
			throw new PermissionDeniedException(currentUser + " has no permisson to task " + task.getId());
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new TaskServiceException(e);
		}
		finally {
		}
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final TaskContext taskContext) throws PermissionDeniedException, LoginRequiredException, TaskServiceException {
		try {
			logger.debug("expectOwner");
			if (taskContext != null) {
				final Collection<UserIdentifier> users = getTaskContextUsers(taskContext);
				authorizationService.expectUser(sessionIdentifier, users);
			}
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException {
		try {
			logger.debug("expectOwner");
			if (taskContextIdentifier != null) {
				expectOwner(sessionIdentifier, taskContextDao.load(taskContextIdentifier));
			}
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException {
		try {
			logger.debug("expectOwner");
			expectOwner(sessionIdentifier, taskDao.load(taskIdentifier));
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public Task getTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getTask for id: " + taskIdentifier);
			authenticationService.expectLoggedIn(sessionIdentifier);

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

		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<Task> getTaskChilds(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectOwner(sessionIdentifier, taskDao.load(taskIdentifier));

			logger.debug("getTaskChilds");
			final Set<Task> result = new HashSet<Task>();
			final EntityIterator<TaskBean> i = taskDao.getTaskChilds(taskIdentifier);
			while (i.hasNext()) {
				final Task task = i.next();
				result.add(task);
			}
			logger.debug("getTasksCompleted found " + result.size());
			return result;
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
	public TaskContext getTaskContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("getTaskContext");
			final TaskContext taskContext = taskContextDao.load(taskContextIdentifier);
			if (taskContext == null) {
				logger.info("taskContext not found with id " + taskContextIdentifier);
				return null;
			}
			expectOwner(sessionIdentifier, taskContext);
			return taskContext;
		}
		catch (final StorageException e) {
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
	public TaskContext getTaskContextByName(final SessionIdentifier sessionIdentifier, final String taskContextName) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("getTaskContextByName");
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
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<UserIdentifier> getTaskContextUsers(final TaskContext taskContext) throws TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getTaskContextUsers - taskcontent: " + taskContext.getId());
			final Set<UserIdentifier> users = new HashSet<UserIdentifier>();
			users.add(taskContext.getOwner());
			final StorageIterator i = taskContextToUserManyToManyRelation.getA(taskContext.getId());
			while (i.hasNext()) {
				final StorageValue a = i.next();
				final String userId = a.getString();
				logger.debug("found user " + userId);
				users.add(new UserIdentifier(userId));
			}
			return users;
		}
		catch (final StorageException e) {
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
	public Collection<UserIdentifier> getTaskContextUsers(final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getTaskContextUsers - taskcontent: " + taskContextIdentifier);
			final TaskContextBean taskContext = taskContextDao.load(taskContextIdentifier);
			return getTaskContextUsers(taskContext);
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
	public Collection<Task> getTasks(final SessionIdentifier sessionIdentifier, final boolean completed) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final Set<Task> result = new HashSet<Task>();

			// owned tasks
			final EntityIterator<TaskBean> ti = taskDao.getTasks(userIdentifier, completed);
			while (ti.hasNext()) {
				result.add(ti.next());
			}

			// shared tasks
			final Collection<TaskContextIdentifier> taskContextIdentifiers = getTaskContextIdentifiers(sessionIdentifier);
			result.addAll(getTasks(sessionIdentifier, completed, taskContextIdentifiers));

			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
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
	public Collection<Task> getTasks(final SessionIdentifier sessionIdentifier, final boolean completed, final Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final Set<Task> result = new HashSet<Task>();

			for (final TaskContextIdentifier taskContextIdentifier : taskContextIdentifiers) {
				final EntityIterator<TaskBean> ti = taskDao.getTasks(taskContextIdentifier, completed);
				while (ti.hasNext()) {
					result.add(ti.next());
				}
			}

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
	public Collection<Task> getTasks(final SessionIdentifier sessionIdentifier, final boolean completed, final TaskFocus taskFocus,
			final Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			logger.debug("get tasks not completed with focus: " + taskFocus + " contexts: " + taskContextIdentifiers);

			final Set<Task> result = new HashSet<Task>();

			for (final TaskContextIdentifier taskContextIdentifier : taskContextIdentifiers) {
				final EntityIterator<TaskBean> ti = taskDao.getTasks(taskContextIdentifier, taskFocus, completed);
				while (ti.hasNext()) {
					result.add(ti.next());
				}
			}

			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
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
	public void removeUserFromContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final UserIdentifier userIdentifier)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectOwner(sessionIdentifier, taskContextIdentifier);
			logger.debug("removeUserFromContext");
			taskContextToUserManyToManyRelation.remove(taskContextIdentifier, userIdentifier);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void saveTaskAndChilds(final TaskBean task) throws StorageException, EntityIteratorException, ValidationException {
		saveTaskAndChilds(null, task);
	}

	private void saveTaskAndChilds(final TaskBean parentTask, final TaskBean task) throws StorageException, EntityIteratorException, ValidationException {
		if (parentTask != null) {
			task.setStart(calendarUtil.max(task.getStart(), parentTask.getStart()));
			task.setDue(calendarUtil.min(task.getDue(), parentTask.getDue()));
			task.setFocus(parentTask.getFocus());
			task.setContext(parentTask.getContext());
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
			saveTaskAndChilds(task, child);
		}
	}

	@Override
	public List<TaskMatch> searchTasks(final SessionIdentifier sessionIdentifier, final int limit, final List<String> words) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Collection<Task> beans = getTasks(sessionIdentifier, false);
		final BeanSearcher<Task> beanSearch = new TaskSearcher();
		final List<BeanMatch<Task>> matches = beanSearch.search(beans, limit, words);
		final List<TaskMatch> result = new ArrayList<TaskMatch>();
		for (final BeanMatch<Task> match : matches) {
			result.add(new TaskMatchImpl(match));
		}
		return result;
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

			logger.debug("swapPrio " + taskIdentifierA + " <=> " + taskIdentifierB);
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
			logger.debug("unCompleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			expectOwner(sessionIdentifier, task);

			if (Boolean.FALSE.equals(task.getCompleted())) {
				logger.info("not completed => skip");
				return;
			}

			task.setCompletionDate(null);
			task.setCompleted(false);
			taskDao.save(task);
		}
		catch (final StorageException e) {
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

			logger.debug("updateTask - id: " + taskDto.getId());

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
			task.setContext(taskDto.getContext());
			task.setFocus(taskDto.getFocus());

			saveTaskAndChilds(parentTask, task);
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
	public void updateTaskContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final String name) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("updateTaskContext");

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

	@Override
	public void updateTaskFocus(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskFocus taskFocus) throws PermissionDeniedException,
			LoginRequiredException, TaskServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("updateTaskFocus - task: " + taskIdentifier + " focus: " + taskFocus);
			TaskBean task = taskDao.load(taskIdentifier);

			while (task.getParentId() != null) {
				task = taskDao.load(task.getParentId());
			}

			task.setFocus(taskFocus);
			saveTaskAndChilds(task);
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
	public Collection<TaskContextIdentifier> getTaskContextIdentifiers(final SessionIdentifier sessionIdentifier) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("updateTaskContext");

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final Set<TaskContextIdentifier> result = new HashSet<TaskContextIdentifier>();
			result.addAll(taskContextDao.getTaskContextIdentifiersByUser(userIdentifier));

			final StorageIterator taskContextIterator = taskContextToUserManyToManyRelation.getB(userIdentifier);
			while (taskContextIterator.hasNext()) {
				final TaskContextIdentifier taskContextIdentifier = new TaskContextIdentifier(taskContextIterator.next().getString());
				result.add(taskContextIdentifier);
			}
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
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
	public Collection<TaskContext> getTaskContexts(final SessionIdentifier sessionIdentifier) throws TaskServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("get all taskContexts for user " + userIdentifier);

			final Set<TaskContext> result = new HashSet<TaskContext>();
			result.addAll(taskContextDao.getTaskContextsByUser(userIdentifier));

			final StorageIterator taskContextIterator = taskContextToUserManyToManyRelation.getB(userIdentifier);
			while (taskContextIterator.hasNext()) {
				final TaskContextIdentifier taskContextIdentifier = new TaskContextIdentifier(taskContextIterator.next().getString());
				result.add(taskContextDao.load(taskContextIdentifier));
			}
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
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
	public Collection<Task> getTasks(final SessionIdentifier sessionIdentifier, final boolean completed, final TaskFocus taskFocus) throws LoginRequiredException,
			TaskServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("get tasks not completed for focus: " + taskFocus + " user: " + userIdentifier);

			final Set<Task> result = new HashSet<Task>();

			// owned tasks
			final EntityIterator<TaskBean> ti = taskDao.getTasks(userIdentifier, taskFocus, completed);
			while (ti.hasNext()) {
				final TaskBean task = ti.next();
				result.add(task);
			}

			// shared tasks
			final Collection<TaskContextIdentifier> taskContextIdentifiers = getTaskContextIdentifiers(sessionIdentifier);
			result.addAll(getTasks(sessionIdentifier, completed, taskFocus, taskContextIdentifiers));

			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
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
	public Collection<Task> getTasksWithoutContext(final SessionIdentifier sessionIdentifier, final boolean completed, final TaskFocus taskFocus) throws LoginRequiredException,
			TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			logger.debug("get tasks not completed without context for user: " + userIdentifier + " and focus: " + taskFocus);

			final Set<Task> result = new HashSet<Task>();

			final EntityIterator<TaskBean> ti = new EntityIteratorFilter<TaskBean>(taskDao.getTasks(userIdentifier, taskFocus, completed), new TaskWithoutContextPredicate<TaskBean>());
			while (ti.hasNext()) {
				result.add(ti.next());
			}

			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
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
	public Collection<Task> getTasksWithoutContext(final SessionIdentifier sessionIdentifier, final boolean completed) throws LoginRequiredException, TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			logger.debug("get tasks not completed without context for user: " + userIdentifier);

			final Set<Task> result = new HashSet<Task>();

			final EntityIterator<TaskBean> ti = new EntityIteratorFilter<TaskBean>(taskDao.getTasks(userIdentifier, completed), new TaskWithoutContextPredicate<TaskBean>());
			while (ti.hasNext()) {
				result.add(ti.next());
			}

			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final EntityIteratorException e) {
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
	public void taskSelectTaskContext(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier)
			throws LoginRequiredException, PermissionDeniedException, ValidationException, TaskServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			expectOwner(sessionIdentifier, taskContextIdentifier);

			while (task.getParentId() != null) {
				task = taskDao.load(task.getParentId());
			}

			task.setContext(taskContextIdentifier);

			saveTaskAndChilds(task);
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
}
