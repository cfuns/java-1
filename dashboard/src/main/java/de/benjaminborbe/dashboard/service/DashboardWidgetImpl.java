package de.benjaminborbe.dashboard.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.website.util.CssResourceImpl;

@Singleton
public class DashboardWidgetImpl implements DashboardWidget {

	private final class ComparatorImplementation implements Comparator<DashboardContentWidget> {

		@Override
		public int compare(final DashboardContentWidget w1, final DashboardContentWidget w2) {
			return w1.getTitle().compareTo(w2.getTitle());
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final DashboardWidgetRegistry dashboardWidgetRegistry;

	@Inject
	public DashboardWidgetImpl(final Logger logger, final DashboardWidgetRegistry dashboardWidgetRegistry) {
		this.logger = logger;
		this.dashboardWidgetRegistry = dashboardWidgetRegistry;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<h1>Dashboard</h1>");
		final List<DashboardContentWidget> dashboardWidgets = new ArrayList<DashboardContentWidget>(dashboardWidgetRegistry.getAll());
		Collections.sort(dashboardWidgets, new ComparatorImplementation());
		for (final DashboardContentWidget dashboardWidget : dashboardWidgets) {
			try {
				printDashboardWidget(request, response, dashboardWidget);
			}
			catch (final Exception e) {
				e.printStackTrace(out);
			}
		}
	}

	protected void printDashboardWidget(final HttpServletRequest request, final HttpServletResponse response, final DashboardContentWidget dashboardWidget) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<div class=\"dashboardWidget\">");
		out.println("<h2>" + dashboardWidget.getTitle() + "</h2>");
		dashboardWidget.render(request, response);
		out.println("</div>");
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<CssResource> result = new ArrayList<CssResource>();
		result.add(new CssResourceImpl(contextPath + "/dashboard/css/style.css"));
		for (final DashboardContentWidget dashboardWidget : dashboardWidgetRegistry.getAll()) {
			if (dashboardWidget instanceof RequireCssResource) {
				result.addAll(((RequireCssResource) dashboardWidget).getCssResource(request, response));
			}
		}
		logger.debug("found " + result + " required css resources");
		return result;
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		for (final DashboardContentWidget dashboardWidget : dashboardWidgetRegistry.getAll()) {
			if (dashboardWidget instanceof RequireJavascriptResource) {
				result.addAll(((RequireJavascriptResource) dashboardWidget).getJavascriptResource(request, response));
			}
		}
		logger.debug("found " + result + " required javascript resources");
		return result;
	}

}
