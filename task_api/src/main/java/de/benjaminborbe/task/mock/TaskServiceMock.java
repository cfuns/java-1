package de.benjaminborbe.task.mock;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;

@Singleton
public class TaskServiceMock implements TaskService {

	@Inject
	public TaskServiceMock() {
	}

	@Override
	public void completeTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
	}

	@Override
	public TaskContextIdentifier createTaskContext(final SessionIdentifier sessionIdentifier, final String name) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public void deleteContextTask(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException {
	}

	@Override
	public void deleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
	}

	@Override
	public Task getTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		return null;
	}

	@Override
	public List<Task> getTaskChilds(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		return null;
	}

	@Override
	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier, final Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException {
		return null;
	}

	@Override
	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public void swapPrio(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifierA, final TaskIdentifier taskIdentifierB) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException {
	}

	@Override
	public void uncompleteTask(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
	}

	@Override
	public void updateTaskContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final String name) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException, ValidationException {
	}

	@Override
	public TaskContextIdentifier createTaskContextIdentifier(final String id) throws TaskServiceException {
		return null;
	}

	@Override
	public TaskIdentifier createTaskIdentifier(final String id) throws TaskServiceException {
		return null;
	}

	@Override
	public TaskContext getTaskContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException,
			PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public TaskIdentifier createTask(final SessionIdentifier sessionIdentifier, final TaskDto taskDto) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public TaskContext getTaskContextByName(final SessionIdentifier sessionIdentifier, final String taskContextName) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public List<TaskMatch> searchTasks(final SessionIdentifier sessionIdentifier, final int limit, final List<String> words) throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public void updateTask(final SessionIdentifier sessionIdentifier, final TaskDto taskDto) throws TaskServiceException, PermissionDeniedException, LoginRequiredException,
			ValidationException {
	}

	@Override
	public void addUserToContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final UserIdentifier userIdentifier)
			throws TaskServiceException {
	}

	@Override
	public void removeUserFromContext(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier, final UserIdentifier userIdentifier)
			throws TaskServiceException {
	}

	@Override
	public Collection<UserIdentifier> getTaskContextUsers(final TaskContextIdentifier taskContextIdentifier) throws TaskServiceException {
		return null;
	}

	@Override
	public Collection<UserIdentifier> getTaskContextUsers(final TaskContext taskContext) throws TaskServiceException {
		return null;
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException {
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final Task task) throws PermissionDeniedException, LoginRequiredException, TaskServiceException {
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final TaskContextIdentifier taskContextIdentifier) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException {
	}

	@Override
	public void expectOwner(final SessionIdentifier sessionIdentifier, final TaskContext taskContext) throws PermissionDeniedException, LoginRequiredException, TaskServiceException {
	}

	@Override
	public void updateTaskFocus(final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskFocus taskFocus) throws PermissionDeniedException,
			LoginRequiredException, TaskServiceException {
	}

	@Override
	public Collection<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final TaskFocus taskFocus, final Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public Collection<TaskContext> getTaskContexts(final SessionIdentifier sessionIdentifier) throws TaskServiceException, PermissionDeniedException, LoginRequiredException {
		return null;
	}

	@Override
	public Collection<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException {
		return null;
	}

	@Override
	public Collection<TaskContextIdentifier> getTaskContextIdentifiers(final SessionIdentifier sessionIdentifier) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException {
		return null;
	}

}
