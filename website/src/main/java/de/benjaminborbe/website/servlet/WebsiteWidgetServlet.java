package de.benjaminborbe.website.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public abstract class WebsiteWidgetServlet extends WebsiteServlet {

	private static final long serialVersionUID = 4493141623771043143L;

	private final Logger logger;

	@Inject
	public WebsiteWidgetServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
		this.logger = logger;
	}

	@Override
	protected void doService(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException, ServletException {
		final Widget widget = createWidget(request, response, context);
		widget.render(request, response, context);
		logger.trace("serice end");
	}

	public abstract Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException;

}
