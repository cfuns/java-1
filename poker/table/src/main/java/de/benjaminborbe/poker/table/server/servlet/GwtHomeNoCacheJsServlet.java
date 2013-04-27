package de.benjaminborbe.poker.table.server.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteResourceServlet;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GwtHomeNoCacheJsServlet extends WebsiteResourceServlet {

	private static final long serialVersionUID = -2162585976374394940L;

	@Inject
	public GwtHomeNoCacheJsServlet(
		final Logger logger,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService
	) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider, authorizationService);
	}

	@Override
	public String getPath() {
		return "/Home/Home.nocache.js";
	}

	@Override
	public String contentType() {
		return "text/javascript";
	}
}
