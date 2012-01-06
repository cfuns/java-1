package de.benjaminborbe.website.servlet;

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

@Singleton
public class WebsiteDashboardServlet extends WebsiteMainServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Dashboard";

	private final DashboardWidget dashboardWidget;

	@Inject
	public WebsiteDashboardServlet(final Logger logger, final CssResourceRenderer cssResourceRenderer, final JavascriptResourceRenderer javascriptResourceRenderer, final DashboardWidget dashboardWidget) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.dashboardWidget = dashboardWidget;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		dashboardWidget.render(request, response);
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<Widget>();
		result.add(dashboardWidget);
		return result;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
