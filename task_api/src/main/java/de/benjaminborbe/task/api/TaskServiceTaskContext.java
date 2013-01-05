package de.benjaminborbe.task.api;

import java.util.Collection;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface TaskServiceTaskContext {

	void addTaskContext(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException;

	TaskContextIdentifier createTaskContext(SessionIdentifier sessionIdentifier, String name) throws TaskServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException;

	TaskContextIdentifier createTaskContextIdentifier(String id) throws TaskServiceException;

	void deleteContextTask(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException;

	TaskContext getTaskContext(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

	TaskContext getTaskContextByName(SessionIdentifier sessionIdentifier, String taskContextName) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<TaskContext> getTaskContexts(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException;

	Collection<TaskContext> getTasksContexts(SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException;

	void updateTaskContext(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier, String name) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException;

	void addUserToContext(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier, UserIdentifier userIdentifier) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException, ValidationException;

	void removeUserFromContext(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier, UserIdentifier userIdentifier) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException;

	Collection<UserIdentifier> getTaskContextUsers(TaskContextIdentifier taskContextIdentifier) throws TaskServiceException;

	Collection<UserIdentifier> getTaskContextUsers(TaskContext taskContext) throws TaskServiceException;
}
