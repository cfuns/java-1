package de.benjaminborbe.authentication.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteRedirectServlet;
import org.slf4j.Logger;

@Singleton
public class AuthenticationGuiServlet extends WebsiteRedirectServlet {

	private static final long serialVersionUID = -4538727884647259439L;

	private static final String TARGET = "authentication/status";

	@Inject
	public AuthenticationGuiServlet(
		final Logger logger,
		final AuthenticationService authenticationService,
		final UrlUtil urlUtil,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final Provider<HttpContext> httpContextProvider,
		final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider, authorizationService);
	}

	@Override
	protected String getTarget() {
		return TARGET;
	}

}
