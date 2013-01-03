package de.benjaminborbe.task.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface TaskService {

	void addTaskContext(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException;

	void completeTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	TaskIdentifier createTask(SessionIdentifier sessionIdentifier, TaskDto taskDto) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	TaskContextIdentifier createTaskContext(SessionIdentifier sessionIdentifier, String name) throws TaskServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException;

	TaskContextIdentifier createTaskContextIdentifier(String id) throws TaskServiceException;

	TaskIdentifier createTaskIdentifier(String id) throws TaskServiceException;

	void deleteContextTask(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException;

	void deleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Task getTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<Task> getTaskChilds(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	TaskContext getTaskContext(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

	TaskContext getTaskContextByName(SessionIdentifier sessionIdentifier, String taskContextName) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<TaskContext> getTaskContexts(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException;

	List<Task> getTasksCompleted(SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException;

	List<Task> getTasksCompleted(SessionIdentifier sessionIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException, LoginRequiredException;

	List<TaskContext> getTasksContexts(SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException;

	void replaceTaskContext(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<TaskMatch> searchTasks(SessionIdentifier sessionIdentifier, int limit, List<String> words) throws TaskServiceException, LoginRequiredException;

	void swapPrio(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifierA, TaskIdentifier taskIdentifierB) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

	void uncompleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void updateTask(SessionIdentifier sessionIdentifier, TaskDto taskDto) throws TaskServiceException, PermissionDeniedException, LoginRequiredException, ValidationException;

	void updateTaskContext(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier, String name) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException;

}
