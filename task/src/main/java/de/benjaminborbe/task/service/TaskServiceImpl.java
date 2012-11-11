package de.benjaminborbe.task.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
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
import de.benjaminborbe.tools.util.ComparatorChain;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class TaskServiceImpl implements TaskService {

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final CalendarUtil calendarUtil;

	private final IdGeneratorUUID idGeneratorUUID;

	private final Logger logger;

	private final TaskContextDao taskContextDao;

	private final TaskContextManyToManyRelation taskContextManyToManyRelation;

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
			final TaskContextManyToManyRelation taskContextManyToManyRelation,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.taskDao = taskDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.taskContextDao = taskContextDao;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.validationExecutor = validationExecutor;
		this.taskContextManyToManyRelation = taskContextManyToManyRelation;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void addTaskContext(final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException {
		try {
			logger.trace("addTaskContext");
			taskContextManyToManyRelation.add(taskIdentifier, taskContextIdentifier);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			logger.trace("completeTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			task.setModified(calendarUtil.now());
			task.setCompletionDate(calendarUtil.now());
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
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final String name, final String description, final TaskIdentifier taskParentIdentifier,
			final Calendar start, final Calendar due) throws TaskServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		try {
			logger.trace("createTask");

			authenticationService.expectLoggedIn(sessionIdentifier);

			// check parent
			if (taskParentIdentifier != null) {
				final TaskBean parentTask = taskDao.load(taskParentIdentifier);
				authorizationService.expectUser(sessionIdentifier, parentTask.getOwner());
			}

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskIdentifier taskIdentifier = createTaskIdentifier(sessionIdentifier, idGeneratorUUID.nextId(name));
			final TaskBean task = taskDao.create();
			task.setId(taskIdentifier);
			task.setName(name);
			task.setDescription(description);
			task.setOwner(userIdentifier);
			task.setCompleted(false);
			task.setModified(calendarUtil.now());
			task.setCreated(calendarUtil.now());
			task.setPriority(taskDao.getMaxPriority(userIdentifier) + 1);
			task.setParentId(taskParentIdentifier);
			task.setDue(due);
			task.setStart(start);
			taskDao.save(task);

			final ValidationResult errors = validationExecutor.validate(task);
			if (errors.hasErrors()) {
				logger.warn("Bookmark " + errors.toString());
				throw new ValidationException(errors);
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
	}

	@Override
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("createTaskContext");

			authenticationService.expectLoggedIn(sessionIdentifier);

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final TaskContextIdentifier taskContextIdentifier = createTaskContextIdentifier(sessionIdentifier, idGeneratorUUID.nextId(name));
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
	public TaskContextIdentifier createTaskContextIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws TaskServiceException {
		if (id != null && id.length() > 0) {
			return new TaskContextIdentifier(id);
		}
		else {
			return null;
		}
	}

	@Override
	public TaskIdentifier createTaskIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws TaskServiceException {
		if (id != null && id.length() > 0) {
			return new TaskIdentifier(id);
		}
		else {
			return null;
		}
	}

	@Override
	public void deleteContextTask(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
		try {
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
	}

	@Override
	public void deleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
		try {
			logger.trace("deleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			if (task == null) {
				logger.trace("task already deleted");
				return;
			}
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
	public Task getTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
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
	}

	@Override
	public List<TaskContext> getTaskContexts(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException {
		try {
			logger.trace("getTaskContexts for task: " + taskIdentifier);
			final StorageIterator i = taskContextManyToManyRelation.getA(taskIdentifier);
			final List<TaskContext> result = new ArrayList<TaskContext>();
			while (i.hasNext()) {
				final String id = i.nextString();
				logger.trace("add taskcontext: " + id);
				final TaskContextBean taskContextBean = taskContextDao.load(createTaskContextIdentifier(sessionIdentifier, id));
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
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("getTasksNotCompleted");
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.trace("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			for (final TaskBean task : taskDao.getTasksNotCompleted(userIdentifier, limit)) {
				result.add(task);
			}
			return sortAndLimit(result, limit);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksNotCompletedWithContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final int limit)
			throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("getTasksNotCompleted for context: " + taskContextIdentifier);
			final List<Task> tasks = getTasksNotCompleted(sessionIdentifier, limit);
			final List<Task> result = new ArrayList<Task>();
			for (final Task task : tasks) {
				if (taskContextManyToManyRelation.exists(task.getId(), taskContextIdentifier)) {
					result.add(task);
				}
			}
			logger.trace("getTasksNotCompleted for context: " + taskContextIdentifier + " found: " + result.size());
			return sortAndLimit(result, limit);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksNotCompletedWithoutContext(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("getTasksNotCompletedWithoutContext");
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.trace("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			for (final TaskBean task : taskDao.getTasksNotCompleted(userIdentifier, limit)) {
				if (!taskContextManyToManyRelation.getA(task.getId()).hasNext()) {
					result.add(task);
				}
			}
			logger.trace("getTasksNotCompletedWithoutContext found " + result.size());
			return sortAndLimit(result, limit);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public void replaceTaskContext(final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException {
		try {
			logger.trace("addTaskContext");
			taskContextManyToManyRelation.removeA(taskIdentifier);
			taskContextManyToManyRelation.add(taskIdentifier, taskContextIdentifier);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	protected List<Task> sortAndLimit(final List<Task> result, final int limit) {
		final List<Comparator<Task>> list = new ArrayList<Comparator<Task>>();
		list.add(new TaskPrioComparator());
		list.add(new TaskNameComparator());
		Collections.sort(result, new ComparatorChain<Task>(list));
		return result.subList(0, Math.min(result.size(), limit));
	}

	@Override
	public void swapPrio(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifierA, final TaskIdentifier taskIdentifierB) throws PermissionDeniedException,
			LoginRequiredException, TaskServiceException {
		try {
			logger.trace("swapPrio " + taskIdentifierA + " <=> " + taskIdentifierB);
			final TaskBean taskA = taskDao.load(taskIdentifierA);
			final TaskBean taskB = taskDao.load(taskIdentifierB);
			authorizationService.expectUser(sessionIdentifier, taskA.getOwner());
			authorizationService.expectUser(sessionIdentifier, taskB.getOwner());
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

	}

	@Override
	public void uncompleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		try {
			logger.trace("unCompleteTask");
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());
			task.setModified(calendarUtil.now());
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
	}

	@Override
	public void updateTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final String name, final String description,
			final TaskIdentifier taskParentIdentifier, final Calendar start, final Calendar due) throws TaskServiceException, PermissionDeniedException, LoginRequiredException {

		try {
			logger.trace("createTask");

			authenticationService.expectLoggedIn(sessionIdentifier);

			// check parent
			if (taskParentIdentifier != null) {
				final TaskBean parentTask = taskDao.load(taskParentIdentifier);
				authorizationService.expectUser(sessionIdentifier, parentTask.getOwner());
			}
			final TaskBean task = taskDao.load(taskIdentifier);
			authorizationService.expectUser(sessionIdentifier, task.getOwner());

			task.setName(name);
			task.setDescription(description);
			task.setModified(calendarUtil.now());
			task.setParentId(taskParentIdentifier);
			task.setDue(due);
			task.setStart(start);
			taskDao.save(task);
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
	}

	@Override
	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("getTasksCompleted");
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.trace("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			for (final TaskBean task : taskDao.getTasksCompleted(userIdentifier, limit)) {
				result.add(task);
			}
			return sortAndLimit(result, limit);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksCompletedWithContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final int limit)
			throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("getTasksCompleted for context: " + taskContextIdentifier);
			final List<Task> tasks = getTasksCompleted(sessionIdentifier, limit);
			final List<Task> result = new ArrayList<Task>();
			for (final Task task : tasks) {
				if (taskContextManyToManyRelation.exists(task.getId(), taskContextIdentifier)) {
					result.add(task);
				}
			}
			logger.trace("getTasksCompleted for context: " + taskContextIdentifier + " found: " + result.size());
			return sortAndLimit(result, limit);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}

	@Override
	public List<Task> getTasksCompletedWithoutContext(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		try {
			logger.trace("getTasksCompletedWithoutContext");
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			logger.trace("user " + userIdentifier);
			final List<Task> result = new ArrayList<Task>();
			for (final TaskBean task : taskDao.getTasksCompleted(userIdentifier, limit)) {
				if (!taskContextManyToManyRelation.getA(task.getId()).hasNext()) {
					result.add(task);
				}
			}
			logger.trace("getTasksCompletedWithoutContext found " + result.size());
			return sortAndLimit(result, limit);
		}
		catch (final AuthenticationServiceException e) {
			throw new TaskServiceException(e);
		}
		catch (final StorageException e) {
			throw new TaskServiceException(e);
		}
	}
}
