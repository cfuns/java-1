package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
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
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;

@Singleton
public class TaskGuiTaskContextUserServlet extends TaskGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 7607369741348885484L;

	private static final String TITLE = "TaskContext - User";

	private final TaskService taskService;

	private final Logger logger;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final AuthenticationService authenticationService;

	@Inject
	public TaskGuiTaskContextUserServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final TaskGuiUtil taskGuiUtil,
			final TaskService taskService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final CacheService cacheService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, taskGuiUtil, cacheService);
		this.taskService = taskService;
		this.logger = logger;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.authenticationService = authenticationService;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String taskContextId = request.getParameter(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID);
			final String userId = request.getParameter(TaskGuiConstants.PARAMETER_USER_ID);
			final TaskContextIdentifier taskContextIdentifier = taskService.createTaskContextIdentifier(taskContextId);
			if (userId != null && taskContextId != null) {
				try {
					final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
					final UserIdentifier userIdentifier = authenticationService.createUserIdentifier(userId);
					taskService.addUserToContext(sessionIdentifier, taskContextIdentifier, userIdentifier);
					throw new RedirectException(taskGuiLinkFactory.taskContextUserUrl(request, taskContextIdentifier));
				}
				catch (final ValidationException e) {
					widgets.add("add user failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			widgets.add(new H2Widget("Users"));
			final List<UserIdentifier> list = new ArrayList<UserIdentifier>(taskService.getTaskContextUsers(taskContextIdentifier));
			final UlWidget ul = new UlWidget();
			for (final UserIdentifier user : list) {
				final ListWidget row = new ListWidget();
				row.add(user.getId());
				row.add(" ");
				row.add(taskGuiLinkFactory.taskContextUserRemove(request, taskContextIdentifier, user));
				ul.add(row);
			}
			widgets.add(ul);

			widgets.add(new H2Widget("Add User"));

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputHiddenWidget(TaskGuiConstants.PARAMETER_TASKCONTEXT_ID));
			formWidget.addFormInputWidget(new FormInputTextWidget(TaskGuiConstants.PARAMETER_USER_ID).addLabel("User:"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("add user"));
			widgets.add(formWidget);
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

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
