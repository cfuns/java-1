package de.benjaminborbe.task.api;

import java.util.List;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface TaskService {

	TaskIdentifier createTask(SessionIdentifier sessionIdentifier, String name, String description) throws TaskServiceException, TaskCreationException, LoginRequiredException;

	Task getTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void deleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void completeTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void unCompleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, int limit) throws TaskServiceException, LoginRequiredException;

	List<Task> getTasksCompleted(SessionIdentifier sessionIdentifier, int limit) throws TaskServiceException, LoginRequiredException;

}
