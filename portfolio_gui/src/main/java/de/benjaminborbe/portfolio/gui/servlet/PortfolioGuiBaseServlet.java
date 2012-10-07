package de.benjaminborbe.portfolio.gui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.servlet.WebsiteWidgetServlet;
import de.benjaminborbe.website.util.BodyWidget;
import de.benjaminborbe.website.util.HtmlWidget;
import de.benjaminborbe.website.widget.HeadWidget;

@Singleton
public abstract class PortfolioGuiBaseServlet extends WebsiteWidgetServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final List<JavascriptResource> javascriptResources = new ArrayList<JavascriptResource>();

	private final List<CssResource> cssResources = new ArrayList<CssResource>();

	@Inject
	public PortfolioGuiBaseServlet(
			final Logger logger,
			final UrlUtil urlUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final Provider<HttpContext> httpContextProvider,
			final AuthenticationService authenticationService) {
		super(logger, urlUtil, calendarUtil, timeZoneUtil, httpContextProvider, authenticationService);
	}

	protected abstract Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException;

	@Override
	public Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final HeadWidget headWidget = new HeadWidget(getTitle(), javascriptResources, cssResources);
		final BodyWidget bodyWidget = new BodyWidget(createContentWidget(request, response, context));
		return new HtmlWidget(headWidget, bodyWidget);
	}

	protected abstract String getTitle();

}
