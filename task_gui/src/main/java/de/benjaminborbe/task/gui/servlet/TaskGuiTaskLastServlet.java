package de.benjaminborbe.task.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
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
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.util.RedirectWidget;

@Singleton
public class TaskGuiTaskLastServlet extends TaskGuiWebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public TaskGuiTaskLastServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final TaskService taskService,
			final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
			PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID));
			logger.debug("move task " + taskIdentifier + " last");
			final List<Task> tasks = new ArrayList<Task>(taskService.getTasks(sessionIdentifier, false));
			final int pos = findPosition(tasks, taskIdentifier);
			logger.debug("found task at pos " + pos);
			for (int i = pos; i < tasks.size(); i++) {
				final Task task = tasks.get(i);
				logger.debug("swap position " + taskIdentifier + " <=> " + task.getId());
				taskService.swapPrio(sessionIdentifier, taskIdentifier, task.getId());
			}
			logger.debug("done");
		}
		catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		catch (final TaskServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}

	private int findPosition(final List<Task> tasks, final TaskIdentifier taskIdentifier) {
		for (int i = 0; i < tasks.size(); ++i) {
			if (tasks.get(i).getId().equals(taskIdentifier)) {
				return i;
			}
		}
		return -1;
	}

}
