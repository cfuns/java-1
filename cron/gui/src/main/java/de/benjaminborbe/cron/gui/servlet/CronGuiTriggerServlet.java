package de.benjaminborbe.cron.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cron.api.CronIdentifier;
import de.benjaminborbe.cron.api.CronService;
import de.benjaminborbe.cron.api.CronServiceException;
import de.benjaminborbe.cron.gui.CronGuiConstants;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CronGuiTriggerServlet extends WebsiteServlet {

	private static final long serialVersionUID = 7727468974460815201L;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final CronService cronService;

	@Inject
	public CronGuiTriggerServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService,
		final CronService cronService
	) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.authenticationService = authenticationService;
		this.logger = logger;
		this.cronService = cronService;
	}

	@Override
	protected void doService(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final HttpContext context
	) throws ServletException, IOException,
		LoginRequiredException, PermissionDeniedException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final CronIdentifier cronIdentifier = cronService.createCronIdentifier(request.getParameter(CronGuiConstants.PARAMETER_CRON_ID));
			cronService.triggerCron(sessionIdentifier, cronIdentifier);
		} catch (final AuthenticationServiceException | CronServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}
}
