package de.benjaminborbe.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.dao.TaskBean;
import de.benjaminborbe.task.dao.TaskContextBean;
import de.benjaminborbe.task.dao.TaskContextDao;
import de.benjaminborbe.task.dao.TaskDao;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class TaskServiceImpl implements TaskService {

	private final Logger logger;

	private final TaskDao taskDao;

	private final TaskContextDao taskContextDao;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskServiceImpl(
			final Logger logger,
			final TaskDao taskDao,
			final TaskContextDao taskContextDao,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.taskDao = taskDao;
		this.taskContextDao = taskContextDao;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final String name, final String description) throws TaskServiceException, LoginRequiredException {
		try {
			logger.debug("createTask");

			authenticationService.expectLoggedIn(sessionIdentifier);

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskIdentifier taskIdentifier = createTaskIdentifier(sessionIdentifier, String.valueOf(UUID.nameUUIDFromBytes(name.getBytes())));
			final TaskBean task = taskDao.create();
			task.setId(taskIdentifier);
			task.setName(name);
			task.setDescription(description);
			task.setOwner(userIdentifier);
			task.setCompleted(false);
			task.setModified(calendarUtil.now());
			task.setCreated(calendarUtil.now());
			taskDao.save(task);
			return taskIdentifier;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public Task getTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			logger.debug("getTask");
			final Task task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			return task;
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void deleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
		try {
			logger.debug("deleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			taskDao.delete(task);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			logger.debug("completeTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			task.setModified(calendarUtil.now());
			task.setCompleted(true);
			taskDao.save(task);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void uncompleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			logger.debug("unCompleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			task.setModified(calendarUtil.now());
			task.setCompleted(false);
			taskDao.save(task);
		}
		catch (final AuthorizationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		try {
			logger.debug("getTasksNotCompleted");
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			for (final TaskBean task : taskDao.getTasksNotCompleted(userIdentifier, limit)) {
				result.add(task);
			}
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		try {
			logger.debug("getTasksCompleted");
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			for (final TaskBean task : taskDao.getTasksCompleted(userIdentifier, limit)) {
				result.add(task);
			}
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public TaskIdentifier createTaskIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws TaskServiceException {
		return new TaskIdentifier(id);
	}

	@Override
	public TaskContextIdentifier createTaskContextIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws TaskServiceException {
		return new TaskContextIdentifier(id);
	}

	@Override
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException {
		try {
			logger.debug("createTaskContext");

			authenticationService.expectLoggedIn(sessionIdentifier);

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskContextIdentifier taskContextIdentifier = createTaskContextIdentifier(sessionIdentifier, String.valueOf(UUID.nameUUIDFromBytes(name.getBytes())));
			final TaskContextBean task = taskContextDao.create();
			task.setId(taskContextIdentifier);
			task.setName(name);
			task.setOwner(userIdentifier);
			task.setModified(calendarUtil.now());
			task.setCreated(calendarUtil.now());
			taskContextDao.save(task);
			return taskContextIdentifier;
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final int limit)
			throws TaskServiceException, LoginRequiredException {
		return getTasksNotCompleted(sessionIdentifier, limit);
	}

	@Override
	public List<TaskContext> getTasksContexts(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException {
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
	}

	@Override
	public void deleteContextTask(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
		try {
			logger.debug("deleteContextTask");
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
	}
}
