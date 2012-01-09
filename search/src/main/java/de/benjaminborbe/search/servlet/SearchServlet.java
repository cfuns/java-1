package de.benjaminborbe.search.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.search.api.SearchWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class SearchServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Search";

	private final SearchWidget searchWidget;

	private final NavigationWidget navigationWidget;

	@Inject
	public SearchServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final SearchWidget searchWidget,
			final NavigationWidget navigationWidget) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer, calendarUtil, timeZoneUtil, parseUtil);
		this.searchWidget = searchWidget;
		this.navigationWidget = navigationWidget;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		navigationWidget.render(request, response, context);
		searchWidget.render(request, response, context);
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<Widget>();
		result.add(navigationWidget);
		result.add(searchWidget);
		return result;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
