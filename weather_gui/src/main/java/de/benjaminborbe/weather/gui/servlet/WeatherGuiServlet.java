package de.benjaminborbe.weather.gui.servlet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteRedirectServlet;

@Singleton
public class WeatherGuiServlet extends WebsiteRedirectServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String DEFAULT_TARGET = "dashboard";

	@Inject
	public WeatherGuiServlet(
			final Logger logger,
			final AuthenticationService authenticationService,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
	}

	@Override
	protected String getTarget() {
		return DEFAULT_TARGET;
	}
}
