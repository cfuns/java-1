package de.benjaminborbe.task.gui.util;

import java.util.ArrayList;
import java.util.Calendar;
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
import de.benjaminborbe.tools.date.CalendarUtil;

public class TaskGuiUtil {

	private final Logger logger;

	private final TaskService taskService;

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskGuiUtil(final Logger logger, final TaskService taskService, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.taskService = taskService;
		this.calendarUtil = calendarUtil;
	}

	public Task getParent(final List<Task> allTasks, final Task task) {
		logger.trace("find parent for: " + task.getId());
		if (task.getParentId() != null) {
			for (final Task parent : allTasks) {
				if (parent.getId().equals(task.getParentId())) {
					return parent;
				}
			}
		}
		return null;
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
				logger.trace("task list for all");
				return taskService.getTasksNotCompleted(sessionIdentifier, taskLimit);
			}
			else {
				logger.trace("task list for context: " + taskContextId);
				final TaskContextIdentifier taskContextIdentifier = taskService.createTaskContextIdentifier(sessionIdentifier, taskContextId);
				return taskService.getTasksNotCompletedWithContext(sessionIdentifier, taskContextIdentifier, taskLimit);
			}
		}
		else {
			logger.trace("task list without context");
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

	public List<Task> filterStart(final List<Task> tasks) {
		final Calendar today = calendarUtil.today();
		final List<Task> result = new ArrayList<Task>();
		for (final Task task : tasks) {
			logger.debug(calendarUtil.toDateTimeString(task.getStart()) + " <= " + calendarUtil.toDateTimeString(today));
			if (task.getStart() == null || calendarUtil.isLE(task.getStart(), today)) {
				result.add(task);
			}
		}
		return result;
	}
}
