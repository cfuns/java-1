package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
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
import de.benjaminborbe.task.gui.widget.TaskGuiSwitchWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TaskGuiTasksCompletedServlet extends TaskGuiHtmlServlet {

	private final class CompareComletionDate extends ComparatorBase<Task, Calendar> {

		@Override
		public Calendar getValue(final Task o) {
			return o.getCompletionDate();
		}

		@Override
		public boolean inverted() {
			return true;
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Tasks - Completed";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final CalendarUtil calendarUtil;

	private final TaskGuiSwitchWidget taskGuiSwitchWidget;

	@Inject
	public TaskGuiTasksCompletedServlet(
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
			final TaskGuiUtil taskGuiUtil,
			final TaskGuiSwitchWidget taskGuiSwitchWidget) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiUtil = taskGuiUtil;
		this.taskGuiSwitchWidget = taskGuiSwitchWidget;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final UlWidget ul = new UlWidget();

			widgets.add(taskGuiSwitchWidget);

			{
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				final String[] taskContextIds = request.getParameterValues(TaskGuiConstants.PARAMETER_SELECTED_TASKCONTEXT_ID);
				final List<Task> tasks = taskGuiUtil.getTasksCompleted(sessionIdentifier, taskContextIds);
				Collections.sort(tasks, new CompareComletionDate());

				for (final Task task : tasks) {
					final ListWidget row = new ListWidget();

					row.add(calendarUtil.toDateTimeString(task.getCompletionDate()));
					row.add(" ");

					final String taskName = taskGuiUtil.buildCompleteName(sessionIdentifier, tasks, task, TaskGuiConstants.PARENT_NAME_LENGTH);
					row.add(new SpanWidget(taskGuiLinkFactory.viewTask(request, taskName, task)).addAttribute("class", "taskTitle"));
					row.add(" ");

					final ListWidget options = new ListWidget();
					options.add(taskGuiLinkFactory.uncompleteTask(request, task));
					options.add(" ");
					options.add(taskGuiLinkFactory.deleteTask(request, task));
					row.add(new SpanWidget(options).addAttribute("class", "taskOptions"));
					ul.add(new DivWidget(row).addClass("taskEntry").addClass("completed"));
				}
				widgets.add(ul);
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
				widgets.add(links);
			}

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
}
