package de.benjaminborbe.task.api;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface TaskService {

	void addTaskContext(TaskIdentifier taskIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException;

	void completeTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	TaskIdentifier createTask(SessionIdentifier sessionIdentifier, String name, String description, String url, TaskIdentifier taskParentIdentifier, Calendar start, Calendar due,
			Long repeatStart, Long repeatDue, Collection<TaskContextIdentifier> contexts) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	TaskContextIdentifier createTaskContext(SessionIdentifier sessionIdentifier, String name) throws TaskServiceException, LoginRequiredException;

	TaskContextIdentifier createTaskContextIdentifier(SessionIdentifier sessionIdentifier, String id) throws TaskServiceException;

	TaskIdentifier createTaskIdentifier(SessionIdentifier sessionIdentifier, String id) throws TaskServiceException;

	void deleteContextTask(SessionIdentifier sessionIdentifier, TaskContextIdentifier taskContextIdentifier) throws LoginRequiredException, TaskServiceException,
			PermissionDeniedException;

	void deleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	Task getTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<Task> getTaskChilds(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	List<TaskContext> getTaskContexts(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException;

	List<Task> getTasksCompleted(SessionIdentifier sessionIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException, LoginRequiredException;

	List<Task> getTasksCompleted(SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException;

	List<TaskContext> getTasksContexts(SessionIdentifier sessionIdentifier) throws LoginRequiredException, TaskServiceException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier, Collection<TaskContextIdentifier> taskContextIdentifiers) throws TaskServiceException,
			LoginRequiredException;

	List<Task> getTasksNotCompleted(SessionIdentifier sessionIdentifier) throws TaskServiceException, LoginRequiredException;

	void replaceTaskContext(TaskIdentifier taskIdentifier, TaskContextIdentifier taskContextIdentifier) throws TaskServiceException;

	void swapPrio(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifierA, TaskIdentifier taskIdentifierB) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException;

	void uncompleteTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws TaskServiceException, LoginRequiredException, PermissionDeniedException;

	void updateTask(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, String name, String description, String url, TaskIdentifier taskParentIdentifier,
			Calendar start, Calendar due, Long repeatStart, Long repeatDue, Collection<TaskContextIdentifier> contexts) throws TaskServiceException, PermissionDeniedException,
			LoginRequiredException, ValidationException;

}
