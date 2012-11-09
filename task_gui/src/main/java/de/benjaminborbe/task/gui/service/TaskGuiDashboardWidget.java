package de.benjaminborbe.task.gui.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

@Singleton
public class TaskGuiDashboardWidget implements DashboardContentWidget, RequireCssResource {

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final StringUtil stringUtil;

	private final TaskGuiUtil taskGuiUtil;

	@Inject
	public TaskGuiDashboardWidget(
			final Logger logger,
			final TaskService taskService,
			final TaskGuiUtil taskGuiUtil,
			final AuthenticationService authenticationService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final StringUtil stringUtil) {
		this.logger = logger;
		this.taskService = taskService;
		this.taskGuiUtil = taskGuiUtil;
		this.authenticationService = authenticationService;
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
					taskService.createTask(sessionIdentifier, name, null, null);
				}
				final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST).addClass("taskdashboardcreate");
				formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_NAME).addPlaceholder("name ...").addValue(""));
				formWidget.addFormInputWidget(new FormInputSubmitWidget("create"));
				widgets.add(formWidget);
			}
			// top tasks
			{
				final UlWidget ul = new UlWidget();
				final List<Task> allTasks = taskService.getTasksNotCompleted(sessionIdentifier, 5);
				final List<Task> tasks = taskGuiUtil.getOnlyChilds(allTasks);

				for (final Task task : tasks) {
					final ListWidget row = new ListWidget();
					row.add(taskGuiLinkFactory.completeTask(request, task));
					row.add(" ");
					row.add(stringUtil.shortenDots(task.getName(), 40));
					ul.add(row);
				}
				widgets.add(ul);
				widgets.add(taskGuiLinkFactory.nextTasks(request));
			}
			widgets.render(request, response, context);
		}
		catch (final Exception e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
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
}
