package de.benjaminborbe.website.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public abstract class WebsiteRedirectServlet extends WebsiteServlet {

	private static final long serialVersionUID = -548691469381588488L;

	protected final Logger logger;

	@Inject
	public WebsiteRedirectServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		logger.trace("service");
		response.sendRedirect(buildRedirectTargetPath(request));
	}

	protected String buildRedirectTargetPath(final HttpServletRequest request) {
		return request.getContextPath() + "/" + getTarget();
	}

	protected abstract String getTarget();
}
