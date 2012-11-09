package de.benjaminborbe.task.gui.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;

public class TaskGuiUtil {

	private final Logger logger;

	private final TaskService taskService;

	@Inject
	public TaskGuiUtil(final Logger logger, final TaskService taskService) {
		this.logger = logger;
		this.taskService = taskService;
	}

	public List<Task> getChildTasks(final List<Task> allTasks, final TaskIdentifier parentId) {
		logger.trace("getChildTasks for parent: " + parentId + " allTasks " + allTasks.size());
		final List<Task> result = new ArrayList<Task>();
		for (final Task task : allTasks) {
			logger.trace("getChildTasks compare " + parentId + " eq " + task.getParentId());
			if ((task.getParentId() == null && parentId == null) || (task.getParentId() != null && parentId != null && task.getParentId().equals(parentId))) {
				logger.trace("match");
				result.add(task);
			}
		}
		logger.trace("getChildTasks for parent: " + parentId + " => found: " + result.size());
		return result;
	}

	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final String taskContextId, final int taskLimit) throws TaskServiceException,
			LoginRequiredException {
		if (taskContextId != null && taskContextId.length() > 0) {
			if ("all".equals(taskContextId)) {
				logger.debug("task list for all");
				return taskService.getTasksNotCompleted(sessionIdentifier, taskLimit);
			}
			else {
				logger.debug("task list for context: " + taskContextId);
				final TaskContextIdentifier taskContextIdentifier = taskService.createTaskContextIdentifier(sessionIdentifier, taskContextId);
				return taskService.getTasksNotCompletedWithContext(sessionIdentifier, taskContextIdentifier, taskLimit);
			}
		}
		else {
			logger.debug("task list without context");
			return taskService.getTasksNotCompletedWithoutContext(sessionIdentifier, taskLimit);
		}
	}

	public List<Task> getOnlyChilds(final List<Task> allTasks) {
		final Set<TaskIdentifier> parents = new HashSet<TaskIdentifier>();
		for (final Task task : allTasks) {
			final TaskIdentifier parentId = task.getParentId();
			if (parentId != null) {
				parents.add(parentId);
			}
		}
		final List<Task> result = new ArrayList<Task>();
		for (final Task task : allTasks) {
			if (!parents.contains(task.getId())) {
				result.add(task);
			}
		}
		return result;
	}
}
