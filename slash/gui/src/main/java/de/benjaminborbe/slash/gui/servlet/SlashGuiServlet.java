package de.benjaminborbe.slash.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SlashGuiServlet extends WebsiteServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String DEFAULT_TARGET = "search";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	@Inject
	public SlashGuiServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		logger.trace("service");
		response.sendRedirect(buildRedirectTargetPath(request));
	}

	protected String buildRedirectTargetPath(final HttpServletRequest request) {
		final String serverName = request.getServerName();
		if (serverName.contains("benjamin-borbe") || serverName.contains("benjaminborbe")) {
			try {
				final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
				if (authenticationService.isLoggedIn(sessionIdentifier)) {
					return request.getContextPath() + "/search";
				}
			} catch (final AuthenticationServiceException e) {
				logger.warn(e.getClass().getName());
			}
			return request.getContextPath() + "/portfolio";
		} else if (serverName.contains("harteslicht.de") || serverName.contains("harteslicht.com")) {
			return request.getContextPath() + "/blog";
		} else if (serverName.contains("rocketnews") || serverName.contains("rocketsource")) {
			return request.getContextPath() + "/wiki";
		} else {
			return request.getContextPath() + "/" + DEFAULT_TARGET;
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	public boolean isLoginRequired() {
		return false;
	}

}
