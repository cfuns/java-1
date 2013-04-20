package de.benjaminborbe.task.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormMethod;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class TaskGuiTaskContextUpdateServlet extends TaskGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "TaskContext - Update";

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	@Inject
	public TaskGuiTaskContextUpdateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final TaskService taskService,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final TaskGuiUtil taskGuiUtil,
		final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, taskGuiUtil, cacheService);
		this.logger = logger;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String name = request.getParameter(TaskGuiConstants.PARAMETER_TASKCONTEXT_NAME);
			final String taskContextId = request.getParameter(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID);
			final TaskContextIdentifier taskContextIdentifier = taskService.createTaskContextIdentifier(taskContextId);
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskContext taskContext = taskService.getTaskContext(sessionIdentifier, taskContextIdentifier);
			if (name != null) {
				try {
					taskService.updateTaskContext(sessionIdentifier, taskContextIdentifier, name.trim());
					throw new RedirectException(taskGuiLinkFactory.taskContextListUrl(request));
				} catch (final ValidationException e) {
					widgets.add("update taskcontext failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget formWidget = new FormWidget().addMethod(FormMethod.POST);
			formWidget.addFormInputWidget(new FormInputHiddenWidget(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID).addValue(taskContext.getId()));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_TASKCONTEXT_NAME).addLabel("Name").addPlaceholder("name...")
				.addDefaultValue(taskContext.getName()));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(formWidget);

			final ListWidget links = new ListWidget();
			links.add(taskGuiLinkFactory.tasksNext(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.tasksUncompleted(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.taskContextList(request));
			widgets.add(links);

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final TaskServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}
}
