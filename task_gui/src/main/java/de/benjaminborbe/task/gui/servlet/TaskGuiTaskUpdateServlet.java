package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
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
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormInputTextareaWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormSelectboxWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class TaskGuiTaskUpdateServlet extends TaskGuiHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Task - Update";

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	@Inject
	public TaskGuiTaskUpdateServlet(
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
			final TaskGuiLinkFactory taskGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.taskService = taskService;
		this.parseUtil = parseUtil;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
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

			final String name = request.getParameter(TaskGuiConstants.PARAMETER_TASK_NAME);
			final String description = request.getParameter(TaskGuiConstants.PARAMETER_TASK_DESCRIPTION);
			final String contextId = request.getParameter(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID);
			final String parentId = request.getParameter(TaskGuiConstants.PARAMETER_TASK_PARENT_ID);
			final String id = request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID);
			final String referer = request.getParameter(TaskGuiConstants.PARAMETER_REFERER);
			final String dueString = request.getParameter(TaskGuiConstants.PARAMETER_TASK_DUE);
			final String startString = request.getParameter(TaskGuiConstants.PARAMETER_TASK_START);
			final String repeatDueString = request.getParameter(TaskGuiConstants.PARAMETER_TASK_REPEAT_DUE);
			final String repeatStartString = request.getParameter(TaskGuiConstants.PARAMETER_TASK_REPEAT_START);
			final String url = request.getParameter(TaskGuiConstants.PARAMETER_TASK_URL);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(sessionIdentifier, id);
			final TaskIdentifier taskParentIdentifier = taskService.createTaskIdentifier(sessionIdentifier, parentId);
			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);

			if (name != null && description != null && contextId != null && parentId != null) {
				try {
					logger.trace("name: " + name);
					final TimeZone timeZone = authenticationService.getTimeZone(sessionIdentifier);

					final Calendar due = parseCalendar(dueString, timeZone);
					final Calendar start = parseCalendar(startString, timeZone);
					final Long repeatDue = parseLong(repeatDueString);
					final Long repeatStart = parseLong(repeatStartString);

					final List<TaskContextIdentifier> contexts = new ArrayList<TaskContextIdentifier>();
					final TaskContextIdentifier taskContextIdentifier = taskService.createTaskContextIdentifier(sessionIdentifier, contextId);
					if (taskContextIdentifier != null) {
						contexts.add(taskContextIdentifier);
					}

					taskService.updateTask(sessionIdentifier, taskIdentifier, name, description, url, taskParentIdentifier, start, due, repeatStart, repeatDue, contexts);

					if (referer != null) {
						throw new RedirectException(referer);
					}
					else {
						throw new RedirectException(taskGuiLinkFactory.createTaskUrl(request, taskParentIdentifier));
					}
				}
				catch (final ValidationException e) {
					widgets.add("add task failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}
			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(TaskGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			formWidget.addFormInputWidget(new FormInputHiddenWidget(TaskGuiConstants.PARAMETER_TASK_ID).addValue(id));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_NAME).addLabel("Name").addPlaceholder("name...").addDefaultValue(task.getName()));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_URL).addLabel("Url").addPlaceholder("url...").addDefaultValue(task.getUrl()));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_PARENT_ID).addLabel("ParentId").addDefaultValue(toValue(task.getParentId())));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_START).addLabel("Start").addPlaceholder("start...")
					.addDefaultValue(toValue(task.getStart())));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_DUE).addLabel("Due").addPlaceholder("due...").addDefaultValue(toValue(task.getDue())));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_REPEAT_START).addLabel("RepeatStart").addPlaceholder("repeat...")
					.addDefaultValue(toValue(task.getRepeatStart())));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASK_REPEAT_DUE).addLabel("RepearDue").addPlaceholder("repeat...")
					.addDefaultValue(toValue(task.getRepeatDue())));

			formWidget.addFormInputWidget(new FormInputTextareaWidget(TaskGuiConstants.PARAMETER_TASK_DESCRIPTION).addLabel("Description").addPlaceholder("description...")
					.addDefaultValue(task.getDescription()));
			final FormSelectboxWidget contextSelectBox = new FormSelectboxWidget(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID).addLabel("Context");
			final List<TaskContext> taskContexts = taskService.getTasksContexts(sessionIdentifier);
			contextSelectBox.addOption("", "none");
			for (final TaskContext taskContext : taskContexts) {
				contextSelectBox.addOption(String.valueOf(taskContext.getId()), taskContext.getName());
			}
			logger.trace("try selected taskcontext");
			for (final TaskContext taskContext : taskService.getTaskContexts(sessionIdentifier, taskIdentifier)) {
				logger.trace("selected taskcontext id: " + taskContext.getId());
				contextSelectBox.addDefaultValue(String.valueOf(taskContext.getId()));
			}
			formWidget.addFormInputWidget(contextSelectBox);
			formWidget.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(formWidget);

			final ListWidget links = new ListWidget();
			links.add(taskGuiLinkFactory.nextTasks(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.uncompletedTasks(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.createTask(request));
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

	private String toValue(final Long value) {
		return value != null ? String.valueOf(value) : "";
	}

	private Calendar parseCalendar(final String dateString, final TimeZone timeZone) {
		try {
			return calendarUtil.parseSmart(timeZone, dateString);
		}
		catch (final ParseException e) {
			return null;
		}
	}

	private String toValue(final TaskIdentifier parentId) {
		return parentId != null ? String.valueOf(parentId) : "";
	}

	private String toValue(final Calendar calendar) {
		return calendar != null ? calendarUtil.toDateTimeString(calendar) : "";
	}

	private Long parseLong(final String value) {
		try {
			return parseUtil.parseLong(value);
		}
		catch (final ParseException e) {
			return null;
		}
	}

}
