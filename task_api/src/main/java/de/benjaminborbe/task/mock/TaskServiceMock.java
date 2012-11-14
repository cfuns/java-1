package de.benjaminborbe.task.mock;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;

@Singleton
public class TaskServiceMock implements TaskService {

	@Inject
	public TaskServiceMock() {
	}

	@Override
	public Task getTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) {
		return null;
	}

	@Override
	public void deleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) {
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) {
	}

	@Override
	public void uncompleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
	}

	@Override
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public TaskIdentifier createTaskIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws TaskServiceException {
		return null;
	}

	@Override
	public TaskContextIdentifier createTaskContextIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws TaskServiceException {
		return null;
	}

	@Override
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public List<Task> getTasksNotCompletedWithContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final int limit)
			throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public List<TaskContext> getTasksContexts(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException {
		return null;
	}

	@Override
	public void deleteContextTask(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
	}

	@Override
	public void addTaskContext(final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException {
	}

	@Override
	public void swapPrio(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifierA, final TaskIdentifier taskIdentifierB) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public List<TaskContext> getTaskContexts(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		return null;
	}

	@Override
	public void replaceTaskContext(final TaskIdentifier taskIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException {
	}

	@Override
	public List<Task> getTasksNotCompletedWithoutContext(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public List<Task> getTasksCompletedWithoutContext(final SessionIdentifier sessionIdentifier, final int limit) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public List<Task> getTasksCompletedWithContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final int limit)
			throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final String name, final String description, final String url,
			final TaskIdentifier taskParentIdentifier, final Calendar start, final Calendar due, final Long repeatStart, final Long repeatDue,
			final Collection<TaskContextIdentifier> contexts) throws TaskServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public void updateTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final String name, final String description, final String url,
			final TaskIdentifier taskParentIdentifier, final Calendar start, final Calendar due, final Long repeatStart, final Long repeatDue,
			final Collection<TaskContextIdentifier> contexts) throws TaskServiceException, PermissionDeniedException, LoginRequiredException, ValidationException {
	}

	@Override
	public List<Task> getTaskChilds(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final int limit) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException {
		return null;
	}

}
