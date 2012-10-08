package de.benjaminborbe.slash.gui.servlet;

import java.io.IOException;

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
import de.benjaminborbe.website.servlet.WebsiteTextServlet;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class SlashGuiRobotsTxtServlet extends WebsiteTextServlet {

	private static final long serialVersionUID = -7647639127591841698L;

	@Inject
	public SlashGuiRobotsTxtServlet(
			final Logger logger,
			final AuthenticationService authenticationService,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider) {
		super(logger, urlUtil, authenticationService, calendarUtil, timeZoneUtil, httpContextProvider);
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add("User-agent: *\n");
		widgets.add("Disallow: /css/\n");
		widgets.add("Disallow: /images/\n");
		return widgets;
	}

	@Override
	protected boolean isLoginRequired() {
		return false;
	}
}
