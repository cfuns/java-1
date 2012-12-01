package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.ServletException;
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
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class TaskGuiTaskStartLaterServlet extends TaskGuiWebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskGuiTaskStartLaterServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final TaskService taskService,
			final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.calendarUtil = calendarUtil;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID));

			final Task task = taskService.getTask(sessionIdentifier, taskIdentifier);
			final TimeZone timeZone = authenticationService.getTimeZone(sessionIdentifier);
			final Calendar now = calendarUtil.now(timeZone);
			final Calendar start = calendarUtil.parseSmart(timeZone, now, request.getParameter(TaskGuiConstants.PARAMETER_TASK_START_LATER));

			taskService.updateTask(sessionIdentifier, taskIdentifier, task.getName(), task.getDescription(), task.getUrl(), task.getParentId(), start, task.getDue(),
					task.getRepeatStart(), task.getRepeatDue(), null);
		}
		catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final TaskServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final LoginRequiredException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final PermissionDeniedException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final ValidationException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final ParseException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}
}
