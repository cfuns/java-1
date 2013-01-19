package de.benjaminborbe.task.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface TaskServiceTask {

	void completeTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	TaskIdentifier createTask(SessionIdentifier sessionIdentifier, TaskDto taskDto) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	TaskIdentifier createTaskIdentifier(String id) throws TaskServiceException;

	void deleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Task getTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Task> getTaskChilds(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Task> getTasksCompleted(SessionIdentifier sessionIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException;

	Collection<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, TaskFocus taskFocus, Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException;

	List<TaskMatch> searchTasks(SessionIdentifier sessionIdentifier, int limit, List<String> words) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void swapPrio(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifierA, TaskIdentifier taskIdentifierB) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

	void uncompleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void updateTask(SessionIdentifier sessionIdentifier, TaskDto taskDto) throws TaskServiceException, PermissionDeniedException, LoginRequiredException, ValidationException;

	void expectOwner(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws PermissionDeniedException, LoginRequiredException, TaskServiceException;

	void expectOwner(SessionIdentifier sessionIdentifier, Task task) throws PermissionDeniedException, LoginRequiredException, TaskServiceException;

	void updateTaskFocus(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, TaskFocus taskFocus) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException, ValidationException;

	Collection<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException;

}
