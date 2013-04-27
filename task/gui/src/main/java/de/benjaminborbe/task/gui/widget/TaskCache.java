package de.benjaminborbe.task.gui.widget;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskCache {

	private final Map<TaskIdentifier, Task> tasks = new HashMap<>();

	private final Logger logger;

	private final TaskService taskService;

	@Inject
	public TaskCache(final Logger logger, final TaskService taskService) {
		this.logger = logger;
		this.taskService = taskService;
	}

	public void addAll(final Collection<Task> tasks) {
		logger.debug("addAll");
		for (final Task task : tasks) {
			add(task);
		}
	}

	public void add(final Task task) {
		logger.trace("add");
		tasks.put(task.getId(), task);
	}

	public Task getParent(
		final SessionIdentifier sessionIdentifier,
		final Task task
	) throws TaskServiceException, LoginRequiredException, PermissionDeniedException {
		logger.trace("find parent for: " + task.getId());
		if (task.getParentId() != null) {
			return get(sessionIdentifier, task.getParentId());
		}
		return null;
	}

	public Task get(
		final SessionIdentifier sessionIdentifier,
		final TaskIdentifier taskIdentifier
	) throws TaskServiceException, LoginRequiredException, PermissionDeniedException {
		if (tasks.containsKey(taskIdentifier)) {
			return tasks.get(taskIdentifier);
		} else {
			final Task result = taskService.getTask(sessionIdentifier, taskIdentifier);
			if (result != null) {
				add(result);
			}
			return result;
		}
	}

	public List<Task> getChildTasks(final TaskIdentifier parentId) {
		final List<Task> result = new ArrayList<>();
		for (final Task task : tasks.values()) {
			logger.trace("getChildTasks compare " + parentId + " eq " + task.getParentId());
			if ((task.getParentId() == null && parentId == null) || (task.getParentId() != null && parentId != null && task.getParentId().equals(parentId))) {
				logger.trace("match");
				result.add(task);
			}
		}
		logger.trace("getChildTasks for parent: " + parentId + " => found: " + result.size());
		return result;
	}
}
