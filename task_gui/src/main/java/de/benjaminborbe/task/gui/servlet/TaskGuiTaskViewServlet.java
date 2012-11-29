package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
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
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.tools.url.UrlUtil;
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

	@Inject
	public TaskGuiTaskViewServlet(
			final Logger logger,
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
			final TaskGuiLinkFactory taskGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.htmlUtil = htmlUtil;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.taskGuiUtil = taskGuiUtil;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.calendarUtil = calendarUtil;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {

		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(sessionIdentifier, request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID));

			{
				final ListWidget tasks = new ListWidget();
				addTask(tasks, sessionIdentifier, taskIdentifier, request);
				widgets.add(new DivWidget(tasks));
			}
			{
				final ListWidget links = new ListWidget();
				links.add(taskGuiLinkFactory.nextTasks(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.uncompletedTasks(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.createTask(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.listTaskContext(request));
				widgets.add(new DivWidget(links));
			}

			return widgets;
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
		if (task.getParentId() != null) {
			addTaskParent(widgets, sessionIdentifier, task.getParentId(), request);
		}
		addTaskEntry(widgets, sessionIdentifier, task, request);
		addChilds(widgets, sessionIdentifier, task.getId(), request);
	}

	private void addChilds(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final HttpServletRequest request)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		final List<Task> childTasks = taskService.getTaskChilds(sessionIdentifier, taskIdentifier);
		for (final Task childTask : childTasks) {
			addTaskEntry(widgets, sessionIdentifier, childTask, request);
			addChilds(widgets, sessionIdentifier, childTask.getId(), request);
		}
	}

	private void addTaskParent(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final HttpServletRequest request)
			throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
		if (task.getParentId() != null) {
			addTaskParent(widgets, sessionIdentifier, task.getParentId(), request);
		}
		addTaskEntry(widgets, sessionIdentifier, task, request);
	}

	private void addTaskEntry(final ListWidget widgets, final SessionIdentifier sessionIdentifier, final Task task, final HttpServletRequest request) throws TaskServiceException,
			LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {

		final String taskName = taskGuiUtil.buildCompleteName(sessionIdentifier, task, Integer.MAX_VALUE);
		final H2Widget title = new H2Widget(taskName);
		if (Boolean.TRUE.equals(task.getCompleted())) {
			title.addAttribute("class", "completed");
		}
		widgets.add(title);
		if (task.getUrl() != null && task.getUrl().length() > 0) {
			widgets.add(new LinkWidget(task.getUrl(), task.getUrl()).addTarget(Target.BLANK));
		}

		if (task.getStart() != null) {
			widgets.add(new DivWidget("Start: " + calendarUtil.toDateTimeString(task.getStart())
					+ (task.getRepeatStart() != null ? " (repeat in + " + task.getRepeatStart() + " days)" : "")));
		}
		if (task.getDue() != null) {
			widgets.add(new DivWidget("Due: " + calendarUtil.toDateTimeString(task.getDue()) + (task.getRepeatDue() != null ? " (repeat in + " + task.getRepeatDue() + " days)" : "")));
		}

		widgets.add(new PreWidget(buildDescription(task.getDescription())));
		if (Boolean.TRUE.equals(task.getCompleted())) {
			widgets.add(taskGuiLinkFactory.uncompleteTask(request, task));
		}
		else {
			widgets.add(taskGuiLinkFactory.completeTask(request, task));
		}
		widgets.add(" ");
		widgets.add(taskGuiLinkFactory.viewTask(request, "view", task));
		widgets.add(" ");
		widgets.add(taskGuiLinkFactory.taskUpdate(request, task));
		widgets.add(" ");
		widgets.add(taskGuiLinkFactory.createSubTask(request, task.getId()));
		widgets.add(" ");
	}

	private Widget buildDescription(final String description) {
		return new HtmlContentWidget(htmlUtil.addLinks(htmlUtil.escapeHtml(description)));
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
