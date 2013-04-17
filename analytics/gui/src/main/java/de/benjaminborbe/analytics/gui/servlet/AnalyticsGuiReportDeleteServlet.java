package de.benjaminborbe.analytics.gui.servlet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import de.benjaminborbe.website.util.RedirectWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AnalyticsGuiReportDeleteServlet extends WebsiteServlet {

	private static final long serialVersionUID = -7862318070826148848L;

	private final AnalyticsService analyticsService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public AnalyticsGuiReportDeleteServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final AnalyticsService analyticsService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.analyticsService = analyticsService;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final AnalyticsReportIdentifier analyticsIdentifier = new AnalyticsReportIdentifier(request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_ID));
			analyticsService.deleteReport(sessionIdentifier, analyticsIdentifier);
		} catch (final AuthenticationServiceException | AnalyticsServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		final RedirectWidget widget = new RedirectWidget(buildRefererUrl(request));
		widget.render(request, response, context);
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			analyticsService.expectAnalyticsAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException | AnalyticsServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
