package de.benjaminborbe.monitoring.servlet;

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
import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringWidget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;

@Singleton
public class MonitoringServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = -3368317580182944276L;

	private static final String TITLE = "Monitoring";

	private final MonitoringWidget monitoringWidget;

	private final NavigationWidget navigationWidget;

	@Inject
	public MonitoringServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final NavigationWidget navigationWidget,
			final MonitoringWidget monitoringWidget) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.navigationWidget = navigationWidget;
		this.monitoringWidget = monitoringWidget;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		navigationWidget.render(request, response);
		monitoringWidget.render(request, response);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<Widget>();
		result.add(navigationWidget);
		result.add(monitoringWidget);
		return result;
	}

}
