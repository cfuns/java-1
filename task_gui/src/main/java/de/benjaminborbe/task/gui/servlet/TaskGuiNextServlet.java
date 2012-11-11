package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
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
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class TaskGuiNextServlet extends TaskGuiHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Tasks - Next";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final ParseUtil parseUtil;

	private final TaskGuiUtil taskGuiUtil;

	@Inject
	public TaskGuiNextServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final TaskGuiWidgetFactory taskGuiWidgetFactory,
			final TaskGuiUtil taskGuiUtil) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.taskGuiUtil = taskGuiUtil;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			widgets.add(taskGuiWidgetFactory.switchTaskContext(request));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String taskContextId = request.getParameter(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
			final int taskLimit = parseUtil.parseInt(request.getParameter(TaskGuiConstants.PARAMETER_TASK_LIMIT), TaskGuiConstants.DEFAULT_TASK_LIMIT);

			final List<Task> allTasks = taskGuiUtil.getTasksNotCompleted(sessionIdentifier, taskContextId, taskLimit);
			final List<Task> childTasks = taskGuiUtil.getOnlyChilds(allTasks);
			final List<Task> tasks = taskGuiUtil.filterStart(childTasks);
			widgets.add(taskGuiWidgetFactory.taskListWithoutParents(sessionIdentifier, tasks, allTasks, request));

			final ListWidget links = new ListWidget();
			links.add(taskGuiLinkFactory.uncompletedTasks(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.createTask(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.completedTasks(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.listTaskContext(request));
			widgets.add(links);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final TaskServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
