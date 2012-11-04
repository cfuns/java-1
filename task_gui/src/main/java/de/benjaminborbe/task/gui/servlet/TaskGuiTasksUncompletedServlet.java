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
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TaskGuiTasksUncompletedServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Tasks";

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final ParseUtil parseUtil;

	@Inject
	public TaskGuiTasksUncompletedServlet(
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
			final TaskService taskService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final TaskGuiWidgetFactory taskGuiWidgetFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.taskService = taskService;
		this.parseUtil = parseUtil;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			widgets.add(taskGuiWidgetFactory.switchTaskContext(request));

			final UlWidget ul = new UlWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final String taskContextId = request.getParameter(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
			final int taskLimit = parseUtil.parseInt(request.getParameter(TaskGuiConstants.PARAMETER_TASK_LIMIT), TaskGuiConstants.DEFAULT_TASK_LIMIT);

			final List<Task> tasks;
			if (taskContextId != null && taskContextId.length() > 0) {
				final TaskContextIdentifier taskContextIdentifier = taskService.createTaskContextIdentifier(sessionIdentifier, taskContextId);
				tasks = taskService.getTasksNotCompleted(sessionIdentifier, taskContextIdentifier, taskLimit);
			}
			else {
				tasks = taskService.getTasksNotCompleted(sessionIdentifier, taskLimit);
			}

			for (final Task task : tasks) {
				final ListWidget row = new ListWidget();
				row.add(task.getName());
				row.add(" ");
				row.add(taskGuiLinkFactory.taskUpdate(request, task));
				row.add(" ");
				row.add(taskGuiLinkFactory.taskPrioIncrease(request, task));
				row.add(" ");
				row.add(taskGuiLinkFactory.taskPrioDecrease(request, task));
				row.add(" ");
				row.add(taskGuiLinkFactory.completeTask(request, task));
				row.add(" ");
				row.add(taskGuiLinkFactory.deleteTask(request, task));
				ul.add(row);
			}
			widgets.add(ul);
			final ListWidget links = new ListWidget();
			links.add(taskGuiLinkFactory.createTask(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.completedTasks(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.listTaskContext(request));
			widgets.add(links);
			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final TaskServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
