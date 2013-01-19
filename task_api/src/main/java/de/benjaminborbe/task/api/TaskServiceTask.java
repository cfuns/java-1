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

	void expectOwner(SessionIdentifier sessionIdentifier, Task task) throws PermissionDeniedException, LoginRequiredException, TaskServiceException;

	void expectOwner(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws PermissionDeniedException, LoginRequiredException, TaskServiceException;

	Task getTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Task> getTaskChilds(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Task> getTasks(SessionIdentifier sessionIdentifier, boolean completed) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Task> getTasks(SessionIdentifier sessionIdentifier, boolean completed, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException;

	Collection<Task> getTasks(SessionIdentifier sessionIdentifier, boolean completed, TaskFocus taskFocus) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException;

	Collection<Task> getTasks(SessionIdentifier sessionIdentifier, boolean completed, TaskFocus taskFocus, Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException;

	Collection<Task> getTasksWithoutContext(SessionIdentifier sessionIdentifier, boolean completed) throws LoginRequiredException, TaskServiceException;

	Collection<Task> getTasksWithoutContext(SessionIdentifier sessionIdentifier, boolean completed, TaskFocus taskFocus) throws LoginRequiredException, TaskServiceException;

	List<TaskMatch> searchTasks(SessionIdentifier sessionIdentifier, int limit, List<String> words) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void swapPrio(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifierA, TaskIdentifier taskIdentifierB) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

	void uncompleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void updateTask(SessionIdentifier sessionIdentifier, TaskDto taskDto) throws TaskServiceException, PermissionDeniedException, LoginRequiredException, ValidationException;

	void updateTaskFocus(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, TaskFocus taskFocus) throws PermissionDeniedException, LoginRequiredException,
			TaskServiceException, ValidationException;

}
