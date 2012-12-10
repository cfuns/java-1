package de.benjaminborbe.task.gui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.StringUtil;

public class TaskGuiUtil {

	private final Logger logger;

	private final TaskService taskService;

	private final StringUtil stringUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskGuiUtil(final Logger logger, final TaskService taskService, final StringUtil stringUtil, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.taskService = taskService;
		this.stringUtil = stringUtil;
		this.calendarUtil = calendarUtil;
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

	public List<TaskContextIdentifier> createTaskContextIdentifiers(final SessionIdentifier sessionIdentifier, final String[] taskContextIds) throws TaskServiceException,
			LoginRequiredException {
		final List<TaskContextIdentifier> result = new ArrayList<TaskContextIdentifier>();
		if (taskContextIds != null) {
			for (final String taskContextId : taskContextIds) {
				result.add(taskService.createTaskContextIdentifier(taskContextId));
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

	public List<Task> filterNotStarted(final List<Task> tasks, final TimeZone timeZone) {
		final TaskStartReadyPredicate taskStartReadyPredicate = new TaskStartReadyPredicate(logger, calendarUtil, timeZone);
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

	public TaskDto quickStringToTask(final SessionIdentifier sessionIdentifier, final String text) throws TaskServiceException, LoginRequiredException {
		final TaskDto task = new TaskDto();
		if (text == null) {
			return task;
		}

		final List<String> remainingTokens = new ArrayList<String>();

		final StringTokenizer st = new StringTokenizer(text, " ");
		while (st.hasMoreTokens()) {
			final boolean hasNext = st.hasMoreTokens();
			final String token = st.nextToken();
			if (hasNext && "context:".equalsIgnoreCase(token)) {
				final String taskContextName = st.nextToken();
				final TaskContext taskContext = taskService.getTaskContextByName(sessionIdentifier, taskContextName);
				task.setContexts(Arrays.asList(taskContext.getId()));
			}
			else if (hasNext && "due:".equalsIgnoreCase(token)) {
				try {
					task.setDue(calendarUtil.parseSmart(st.nextToken()));
				}
				catch (final ParseException e) {
				}
			}
			else if (hasNext && "start:".equalsIgnoreCase(token)) {
				try {
					task.setStart(calendarUtil.parseSmart(st.nextToken()));
				}
				catch (final ParseException e) {
				}
			}
			else {
				remainingTokens.add(token);
			}
		}

		task.setName(StringUtils.join(remainingTokens, " ").replaceAll("\\s+", " ").trim());
		return task;
	}
}
