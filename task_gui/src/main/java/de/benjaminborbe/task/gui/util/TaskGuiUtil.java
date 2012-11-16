package de.benjaminborbe.task.gui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.tools.util.StringUtil;

public class TaskGuiUtil {

	private final Logger logger;

	private final TaskService taskService;

	private final TaskStartReadyPredicate taskStartReadyPredicate;

	private final StringUtil stringUtil;

	@Inject
	public TaskGuiUtil(final Logger logger, final TaskService taskService, final TaskStartReadyPredicate taskStartReadyPredicate, final StringUtil stringUtil) {
		this.logger = logger;
		this.taskService = taskService;
		this.taskStartReadyPredicate = taskStartReadyPredicate;
		this.stringUtil = stringUtil;
	}

	public String buildCompleteName(final SessionIdentifier sessionIdentifier, final List<Task> allTasks, final Task task, final int nameLength) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException {
		final List<String> names = new ArrayList<String>();
		Task parent = getParent(sessionIdentifier, allTasks, task);
		while (parent != null) {
			names.add(stringUtil.shortenDots(parent.getName(), nameLength));
			parent = getParent(sessionIdentifier, allTasks, parent);
		}
		Collections.reverse(names);
		names.add(task.getName());
		return StringUtils.join(names, " / ");
	}

	public Task getParent(final SessionIdentifier sessionIdentifier, final List<Task> allTasks, final Task task) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		logger.trace("find parent for: " + task.getId());
		if (task.getParentId() != null) {
			for (final Task parent : allTasks) {
				if (parent.getId().equals(task.getParentId())) {
					return parent;
				}
			}
			return taskService.getTask(sessionIdentifier, task.getParentId());
		}
		return null;
	}

	public boolean hasChildTasks(final List<Task> allTasks, final TaskIdentifier parentId) {
		for (final Task task : allTasks) {
			if (task.getParentId() != null && parentId != null && task.getParentId().equals(parentId)) {
				return true;
			}
		}
		return false;
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

	public List<Task> getTasksNotCompleted(final SessionIdentifier sessionIdentifier, final String[] taskContextIds) throws TaskServiceException, LoginRequiredException {
		logger.trace("task list for context: " + taskContextIds);
		return taskService.getTasksNotCompleted(sessionIdentifier, createTaskContextIdentifiers(sessionIdentifier, taskContextIds));
	}

	public List<TaskContextIdentifier> createTaskContextIdentifiers(final SessionIdentifier sessionIdentifier, final String[] taskContextIds) throws TaskServiceException {
		final List<TaskContextIdentifier> result = new ArrayList<TaskContextIdentifier>();
		if (taskContextIds != null) {
			for (final String taskContextId : taskContextIds) {
				result.add(taskService.createTaskContextIdentifier(sessionIdentifier, taskContextId));
			}
		}
		return result;
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
		final List<Task> result = new ArrayList<Task>();
		for (final Task task : tasks) {
			if (taskStartReadyPredicate.apply(task)) {
				result.add(task);
			}
		}
		return result;
	}

	public List<Task> getTasksCompleted(final SessionIdentifier sessionIdentifier, final String[] taskContextIds) throws TaskServiceException, LoginRequiredException {
		logger.trace("task list for context: " + taskContextIds);
		return taskService.getTasksCompleted(sessionIdentifier, createTaskContextIdentifiers(sessionIdentifier, taskContextIds));

	}

	public String buildCompleteName(final SessionIdentifier sessionIdentifier, final Task task, final int nameLength) throws TaskServiceException, LoginRequiredException,
			PermissionDeniedException {
		return buildCompleteName(sessionIdentifier, new ArrayList<Task>(), task, nameLength);
	}
}
