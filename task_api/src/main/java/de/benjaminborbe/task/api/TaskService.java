package de.benjaminborbe.task.api;

import java.util.List;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface TaskService {

	TaskIdentifier createTaskIdentifier(SessionIdentifier sessionIdentifier, String id) throws TaskServiceException;

	TaskIdentifier createTask(SessionIdentifier sessionIdentifier, String name, String description, TaskIdentifier taskParentIdentifier) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException;

	Task getTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void deleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void completeTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void uncompleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, int limit) throws TaskServiceException, LoginRequiredException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier, int limit) throws TaskServiceException, LoginRequiredException;

	List<Task> getTasksCompleted(SessionIdentifier sessionIdentifier, int limit) throws TaskServiceException, LoginRequiredException;

	TaskContextIdentifier createTaskContextIdentifier(SessionIdentifier sessionIdentifier, String id) throws TaskServiceException;

	TaskContextIdentifier createTaskContext(SessionIdentifier sessionIdentifier, String name) throws TaskServiceException, LoginRequiredException;

	List<TaskContext> getTasksContexts(SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException;

	void deleteContextTask(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException;

	void addTaskContext(TaskIdentifier taskIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException;

	void swapPrio(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifierA, TaskIdentifier taskIdentifierB) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

}
