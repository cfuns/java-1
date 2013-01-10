package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskComparator;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.PreWidget;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class TaskGuiTaskViewServlet extends TaskGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 4130890931474576938L;

	private static final String TITLE = "Task - View";

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiUtil taskGuiUtil;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final HtmlUtil htmlUtil;

	private final CalendarUtil calendarUtil;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final ComparatorUtil comparatorUtil;

	private final TaskComparator taskComparator;

	@Inject
	public TaskGuiTaskViewServlet(
			final Logger logger,
			final TaskGuiWidgetFactory taskGuiWidgetFactory,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final HtmlUtil htmlUtil,
			final TaskService taskService,
			final TaskGuiUtil taskGuiUtil,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final ComparatorUtil comparatorUtil,
			final TaskComparator taskComparator) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, taskGuiUtil);
		this.logger = logger;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.htmlUtil = htmlUtil;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.taskGuiUtil = taskGuiUtil;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.calendarUtil = calendarUtil;
		this.comparatorUtil = comparatorUtil;
		this.taskComparator = taskComparator;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {

		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID));
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			if (task == null) {
				return new RedirectWidget(taskGuiLinkFactory.tasksNextUrl(request));
			}
			{
				final ListWidget tasks = new ListWidget();
				addTask(tasks, sessionIdentifier, task, request);
				widgets.add(new DivWidget(tasks));
			}
			{
				final ListWidget links = new ListWidget();
				links.add(taskGuiLinkFactory.tasksNext(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.tasksUncompleted(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.taskCreate(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.taskContextList(request));
				widgets.add(new DivWidget(links));
			}

			return new DivWidget(widgets).addClass("taskView");
		}
		catch (final TaskServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void addTask(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final HttpServletRequest request)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
		addTask(widgets, sessionIdentifier, task, request);
	}

	private void addTask(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final Task task, final HttpServletRequest request) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		if (task.getParentId() != null) {
			addTask(widgets, sessionIdentifier, task.getParentId(), request);
		}
		else {
			addChilds(widgets, sessionIdentifier, task, request, null, null);
		}
	}

	private void addChilds(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final Task task, final HttpServletRequest request, final Task previousTask,
			final Task nextTask) throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		final List<Task> childTasks = comparatorUtil.sort(taskService.getTaskChilds(sessionIdentifier, task.getId()), taskComparator);
		addTaskEntry(widgets, sessionIdentifier, task, request, hasNotCompleted(childTasks), nextTask, previousTask);

		if (childTasks.size() > 0) {
			for (int i = 0; i < childTasks.size(); ++i) {
				final Task childPreviousTask = getTask(childTasks, i + 1);
				final Task childNextTask = getTask(childTasks, i - 1);
				final Task childTask = childTasks.get(i);
				addChilds(widgets, sessionIdentifier, childTask, request, childPreviousTask, childNextTask);
			}
		}
	}

	private Task getTask(final List<Task> tasks, final int pos) {
		if (pos < 0 || pos >= tasks.size()) {
			return null;
		}
		return tasks.get(pos);
	}

	private boolean hasNotCompleted(final Collection<Task> tasks) {
		for (final Task task : tasks) {
			if (Boolean.FALSE.equals(task.getCompleted()) || task.getCompleted() == null) {
				return false;
			}
		}
		return true;
	}

	private void addTaskEntry(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final Task task, final HttpServletRequest request,
			final boolean hasNotCompletedChilds, final Task previousTask, final Task nextTask) throws TaskServiceException, LoginRequiredException, PermissionDeniedException,
			MalformedURLException, UnsupportedEncodingException {

		final String taskName = taskGuiUtil.buildCompleteName(sessionIdentifier, task, Integer.MAX_VALUE);
		final H2Widget title = new H2Widget(taskName);
		if (Boolean.TRUE.equals(task.getCompleted())) {
			title.addAttribute("class", "completed");
		}
		widgets.add(title);

		final ListWidget options = new ListWidget();

		if (previousTask != null) {
			options.add(taskGuiLinkFactory.taskPrioFirst(request, taskGuiWidgetFactory.buildImage(request, "first"), task.getId()));
			options.add(" ");
			options.add(taskGuiLinkFactory.taskPrioSwap(request, taskGuiWidgetFactory.buildImage(request, "up"), task.getId(), previousTask.getId()));
			options.add(" ");
		}
		if (nextTask != null) {
			options.add(taskGuiLinkFactory.taskPrioSwap(request, taskGuiWidgetFactory.buildImage(request, "down"), task.getId(), nextTask.getId()));
			options.add(" ");
			options.add(taskGuiLinkFactory.taskPrioLast(request, taskGuiWidgetFactory.buildImage(request, "last"), task.getId()));
			options.add(" ");
		}

		if (Boolean.TRUE.equals(task.getCompleted())) {
			options.add(taskGuiLinkFactory.taskUncomplete(request, task));
			options.add(" ");
		}
		else {
			if (hasNotCompletedChilds) {
				options.add(taskGuiLinkFactory.taskComplete(request, taskGuiWidgetFactory.buildImage(request, "complete"), task));
				options.add(" ");
				options.add(taskGuiLinkFactory.taskDelete(request, taskGuiWidgetFactory.buildImage(request, "delete"), task));
				options.add(" ");
			}
		}
		options.add(taskGuiLinkFactory.taskUpdate(request, taskGuiWidgetFactory.buildImage(request, "update"), task));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskCreateSubTask(request, taskGuiWidgetFactory.buildImage(request, "subtask"), task.getId()));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.INBOX, "inbox"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.TODAY, "today"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.NEXT, "next"));
		options.add(" ");
		options.add(taskGuiLinkFactory.taskUpdateFocus(request, task, TaskFocus.SOMEDAY, "someday"));
		widgets.add(new DivWidget(options).addClass("options"));

		if (task.getUrl() != null && task.getUrl().length() > 0) {
			widgets.add(new DivWidget(new LinkWidget(task.getUrl(), task.getUrl()).addTarget(Target.BLANK)));
		}
		if (task.getStart() != null) {
			widgets.add(new DivWidget("Start: " + calendarUtil.toDateTimeString(task.getStart()) + " " + calendarUtil.getWeekday(task.getStart())
					+ (task.getRepeatStart() != null ? " (repeat in + " + task.getRepeatStart() + " days)" : "")));
		}
		if (task.getDue() != null) {
			widgets.add(new DivWidget("Due: " + calendarUtil.toDateTimeString(task.getDue()) + " " + calendarUtil.getWeekday(task.getDue())
					+ (task.getRepeatDue() != null ? " (repeat in + " + task.getRepeatDue() + " days)" : "")));
		}
		if (task.getDescription() != null) {
			widgets.add(new PreWidget(buildDescription(task.getDescription())));
		}
	}

	private Widget buildDescription(final String description) {
		return new HtmlContentWidget(htmlUtil.addLinks(htmlUtil.escapeHtml(description)));
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
