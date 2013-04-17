package de.benjaminborbe.task.gui.servlet;

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
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.util.RedirectWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class TaskGuiTaskUpdateFocusServlet extends TaskGuiWebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public TaskGuiTaskUpdateFocusServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final TaskService taskService,
		final AuthorizationService authorizationService,
		final ParseUtil parseUtil) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
		LoginRequiredException, PermissionDeniedException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID));
			final TaskFocus taskFocus = parseUtil.parseEnum(TaskFocus.class, request.getParameter(TaskGuiConstants.PARAMETER_TASK_FOCUS));
			taskService.updateTaskFocus(taskIdentifier, taskFocus);
		} catch (final AuthenticationServiceException | ValidationException | ParseException | TaskServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}
}
