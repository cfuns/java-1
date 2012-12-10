package de.benjaminborbe.task.gui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskDto;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskDueExpiredPredicate;
import de.benjaminborbe.task.gui.util.TaskDueTodayPredicate;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.task.gui.util.TaskStartReadyPredicate;
import de.benjaminborbe.task.tools.TaskComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.StringWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TaskGuiDashboardWidget implements DashboardContentWidget, RequireCssResource {

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final StringUtil stringUtil;

	private final TaskGuiUtil taskGuiUtil;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final CalendarUtil calendarUtil;

	private final ComparatorUtil comparatorUtil;

	private final TaskComparator taskComparator;

	@Inject
	public TaskGuiDashboardWidget(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TaskComparator taskComparator,
			final ComparatorUtil comparatorUtil,
			final TaskService taskService,
			final TaskGuiUtil taskGuiUtil,
			final AuthenticationService authenticationService,
			final TaskGuiWidgetFactory taskGuiWidgetFactory,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final StringUtil stringUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.taskComparator = taskComparator;
		this.comparatorUtil = comparatorUtil;
		this.taskService = taskService;
		this.taskGuiUtil = taskGuiUtil;
		this.authenticationService = authenticationService;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.stringUtil = stringUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final ListWidget widgets = new ListWidget();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			// quick add
			{
				final String name = request.getParameter(TaskGuiConstants.PARAMETER_TASK_NAME);
				if (name != null) {
					final TaskDto task = new TaskDto();
					task.setName(name);
					taskService.createTask(sessionIdentifier, task);
				}
				final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST).addClass("taskdashboardcreate");
				formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_NAME).addPlaceholder("name...").addValue("").setBr(false));
				formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
				widgets.add(formWidget);
			}
			// top tasks
			{
				final UlWidget ul = new UlWidget();

				final List<Task> tasks = getTasks(sessionIdentifier);
				for (final Task task : tasks) {
					final ListWidget row = new ListWidget();
					row.add(taskGuiLinkFactory.taskComplete(request, taskGuiWidgetFactory.buildImage(request, "complete"), task));
					row.add(" ");
					row.add(taskGuiLinkFactory.taskDelete(request, taskGuiWidgetFactory.buildImage(request, "delete"), task));
					ul.add(row);
					row.add(taskGuiLinkFactory.taskView(request, new StringWidget(stringUtil.shortenDots(task.getName(), 40)), task));
					row.add(" ");
				}
				widgets.add(ul);
				widgets.add(taskGuiLinkFactory.tasksNext(request));
			}
			widgets.render(request, response, context);
		}
		catch (final Exception e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}

	private List<Task> getTasks(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, TaskServiceException, LoginRequiredException {
		final TimeZone timeZone = authenticationService.getTimeZone(sessionIdentifier);
		final List<Task> allTasks = taskService.getTasksNotCompleted(sessionIdentifier);
		final List<Task> childTasks = taskGuiUtil.getOnlyChilds(allTasks);
		final TaskStartReadyPredicate taskStartReadyPredicate = new TaskStartReadyPredicate(logger, calendarUtil, timeZone);
		final TaskDueExpiredPredicate taskDueExpiredPredicate = new TaskDueExpiredPredicate(logger, calendarUtil, timeZone);
		final TaskDueTodayPredicate taskDueTodayPredicate = new TaskDueTodayPredicate(logger, calendarUtil, timeZone);
		final List<Task> tasks = comparatorUtil.sort(
				Collections2.filter(childTasks, Predicates.and(taskStartReadyPredicate, Predicates.or(taskDueTodayPredicate, taskDueExpiredPredicate))), taskComparator);
		return tasks.subList(0, Math.min(tasks.size(), 5));
	}

	@Override
	public String getTitle() {
		return "Tasks";
	}

	@Override
	public long getPriority() {
		return 1;
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final List<CssResource> list = new ArrayList<CssResource>();
		list.add(new CssResourceImpl(request.getContextPath() + "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CSS_STYLE));
		return list;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public String getName() {
		return TaskGuiConstants.NAME;
	}

}
