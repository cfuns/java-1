package de.benjaminborbe.task.gui.util;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.widget.TaskCache;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class TaskGuiUtil {

	private final Logger logger;

	private final TaskService taskService;

	private final StringUtil stringUtil;

	private final CalendarUtil calendarUtil;

	private final UrlUtil urlUtil;

	private final ParseUtil parseUtil;

	@Inject
	public TaskGuiUtil(
		final Logger logger,
		final TaskService taskService,
		final StringUtil stringUtil,
		final CalendarUtil calendarUtil,
		final UrlUtil urlUtil,
		final ParseUtil parseUtil) {
		this.logger = logger;
		this.taskService = taskService;
		this.stringUtil = stringUtil;
		this.calendarUtil = calendarUtil;
		this.urlUtil = urlUtil;
		this.parseUtil = parseUtil;
	}

	public String buildCompleteName(final SessionIdentifier sessionIdentifier, final TaskCache taskCache, final Task task, final int nameLength) throws TaskServiceException,
		LoginRequiredException, PermissionDeniedException {
		final List<String> names = new ArrayList<String>();
		Task parent = taskCache.getParent(sessionIdentifier, task);
		while (parent != null) {
			names.add(stringUtil.shortenDots(parent.getName(), nameLength));
			parent = taskCache.getParent(sessionIdentifier, parent);
		}
		Collections.reverse(names);
		names.add(task.getName());
		return StringUtils.join(names, " / ");
	}

	public boolean hasChildTasks(final List<Task> allTasks, final TaskIdentifier parentId) {
		for (final Task task : allTasks) {
			if (task.getParentId() != null && parentId != null && task.getParentId().equals(parentId)) {
				return true;
			}
		}
		return false;
	}

	public Collection<Task> getTasksWithFocus(final SessionIdentifier sessionIdentifier, final boolean completed, final TaskFocus taskFocus, final List<String> taskContextIds)
		throws TaskServiceException, LoginRequiredException, PermissionDeniedException {
		logger.debug("task list for context: " + taskContextIds);
		if (taskContextIds.size() == 1 && TaskGuiConstants.ALL.equals(taskContextIds.get(0))) {
			logger.debug("get tasks with focus " + taskFocus);
			return taskService.getTasks(sessionIdentifier, completed, taskFocus);
		} else if (taskContextIds.size() == 1 && TaskGuiConstants.NONE.equals(taskContextIds.get(0))) {
			logger.debug("get tasks without context and focus " + taskFocus);
			return taskService.getTasksWithoutContext(sessionIdentifier, completed, taskFocus);
		} else {
			logger.debug("get tasks with contexts " + taskContextIds + " and focus " + taskFocus);
			return taskService.getTasks(sessionIdentifier, completed, taskFocus, createTaskContextIdentifiers(taskContextIds));
		}
	}

	public List<TaskContextIdentifier> createTaskContextIdentifiers(final List<String> taskContextIds) throws TaskServiceException, LoginRequiredException {
		final List<TaskContextIdentifier> result = new ArrayList<TaskContextIdentifier>();
		if (taskContextIds != null) {
			for (final String taskContextId : taskContextIds) {
				result.add(taskService.createTaskContextIdentifier(taskContextId));
			}
		}
		return result;
	}

	public List<Task> getOnlyChilds(final Collection<Task> allTasks) {
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

	public Collection<Task> getTasks(final SessionIdentifier sessionIdentifier, final boolean completed, final List<String> taskContextIds) throws TaskServiceException,
		LoginRequiredException, PermissionDeniedException {
		logger.debug("get completed tasks for context: " + taskContextIds);
		if (taskContextIds.size() == 1 && TaskGuiConstants.ALL.equals(taskContextIds.get(0))) {
			logger.debug("get tasks completed");
			return taskService.getTasks(sessionIdentifier, completed);
		} else if (taskContextIds.size() == 1 && TaskGuiConstants.NONE.equals(taskContextIds.get(0))) {
			logger.debug("get tasks without context");
			return taskService.getTasksWithoutContext(sessionIdentifier, completed);
		} else {
			logger.debug("get tasks with contexts " + taskContextIds);
			return taskService.getTasks(sessionIdentifier, completed, createTaskContextIdentifiers(taskContextIds));
		}

	}

	public TaskDto quickStringToTask(final SessionIdentifier sessionIdentifier, final String text) throws TaskServiceException, LoginRequiredException, PermissionDeniedException {
		final TaskDto task = new TaskDto();
		if (text == null) {
			return task;
		}
		task.setFocus(TaskFocus.INBOX);

		final List<String> remainingTokens = new ArrayList<String>();

		final StringTokenizer st = new StringTokenizer(text, " ");
		while (st.hasMoreTokens()) {
			final boolean hasNext = st.hasMoreTokens();
			final String token = st.nextToken();
			if (hasNext && "context:".equalsIgnoreCase(token)) {
				final String taskContextName = st.nextToken();
				logger.debug("taskContextName: " + taskContextName);
				final TaskContext taskContext = taskService.getTaskContextByName(sessionIdentifier, taskContextName);
				if (taskContext != null) {
					task.setContext(taskContext.getId());
				}
			} else if (hasNext && "due:".equalsIgnoreCase(token)) {
				final String nextToken = st.nextToken();
				logger.debug("due: " + nextToken);
				try {
					task.setDue(calendarUtil.parseSmart(nextToken));
				} catch (final ParseException e) {
					logger.debug("parseSmart token " + nextToken + " failed", e);
				}
			} else if (hasNext && "start:".equalsIgnoreCase(token)) {
				final String nextToken = st.nextToken();
				logger.debug("start: " + nextToken);
				try {
					task.setStart(calendarUtil.parseSmart(nextToken));
				} catch (final ParseException e) {
					logger.debug("parseSmart token " + nextToken + " failed", e);
				}
			} else if (hasNext && "url:".equalsIgnoreCase(token)) {
				final String nextToken = st.nextToken();
				logger.debug("url: " + nextToken);
				if (urlUtil.isUrl(nextToken)) {
					task.setUrl(nextToken);
				} else {
					logger.debug("invalid url: " + nextToken);
				}
			} else if (hasNext && "focus:".equalsIgnoreCase(token)) {
				final String nextToken = st.nextToken();
				logger.debug("focus: " + nextToken);
				try {
					task.setFocus(parseUtil.parseEnum(TaskFocus.class, nextToken.toUpperCase()));
				} catch (final ParseException e) {
					logger.debug("parseEnum token " + nextToken + " failed", e);
				}
			} else {
				remainingTokens.add(token);
			}
		}

		task.setName(StringUtils.join(remainingTokens, " ").replaceAll("\\s+", " ").trim());
		return task;
	}

	public List<String> getSelectedTaskContextIds(final HttpServletRequest request) {
		final String[] list = request.getParameterValues(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
		if (list != null && list.length > 0) {
			logger.trace("use parameter");
			return filterEmpty(list);
		}
		for (final Cookie cookie : request.getCookies()) {
			if (TaskGuiConstants.COOKIE_TASKCONTEXTS.equals(cookie.getName()) && cookie.getValue() != null) {
				logger.debug("found cookie");
				return filterEmpty(cookie.getValue().split(","));
			}
		}
		logger.debug("nothing found");
		return filterEmpty(new String[0]);
	}

	private List<String> filterEmpty(final String... values) {
		final List<String> result = new ArrayList<String>();
		for (final String value : values) {
			if (value != null && value.length() > 0) {
				result.add(value);
			}
		}
		return result;
	}

	public TaskFocus getSelectedTaskFocus(final HttpServletRequest request) {
		final String focus = request.getParameter(TaskGuiConstants.PARAMETER_SELECTED_TASKFOCUS);
		return parseUtil.parseEnum(TaskFocus.class, focus, TaskFocus.INBOX);
	}
}
