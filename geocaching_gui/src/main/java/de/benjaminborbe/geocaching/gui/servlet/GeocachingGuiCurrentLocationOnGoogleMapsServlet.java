package de.benjaminborbe.geocaching.gui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;

@Singleton
public class GeocachingGuiCurrentLocationOnGoogleMapsServlet extends WebsiteServlet {

	private static final long serialVersionUID = -538341985801496273L;

	private final ResourceUtil resourceUtil;

	@Inject
	public GeocachingGuiCurrentLocationOnGoogleMapsServlet(
			final ResourceUtil resourceUtil,
			final Logger logger,
			final UrlUtil urlUtil,
			final AuthenticationService authenticationService,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthorizationService authorizationService) {
		super(logger, urlUtil, authenticationService, authorizationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.resourceUtil = resourceUtil;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws ServletException, IOException {
		response.setContentType("text/html");
		resourceUtil.copyResourceToOutputStream("geo.html", response.getOutputStream());
	}
}
