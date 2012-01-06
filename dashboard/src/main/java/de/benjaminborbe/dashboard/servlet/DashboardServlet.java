package de.benjaminborbe.dashboard.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.html.api.CssResourceRenderer;
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class DashboardServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Dashboard";

	private final DashboardWidget dashboardWidget;

	private final NavigationWidget navigationWidget;

	@Inject
	public DashboardServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final DashboardWidget dashboardWidget,
			final NavigationWidget navigationWidget) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.dashboardWidget = dashboardWidget;
		this.navigationWidget = navigationWidget;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		navigationWidget.render(request, response);
		dashboardWidget.render(request, response);
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<Widget>();
		result.add(navigationWidget);
		result.add(dashboardWidget);
		return result;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
