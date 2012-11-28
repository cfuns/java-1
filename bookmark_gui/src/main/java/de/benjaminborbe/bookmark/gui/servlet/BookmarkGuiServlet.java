package de.benjaminborbe.bookmark.gui.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteRedirectServlet;

@Singleton
public class BookmarkGuiServlet extends WebsiteRedirectServlet {

	private static final long serialVersionUID = -4538727884647259439L;

	private static final String TARGET = "bookmark/list";

	@Inject
	public BookmarkGuiServlet(
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

	@Override
	protected boolean isAdminRequired() {
		return false;
	}

}
