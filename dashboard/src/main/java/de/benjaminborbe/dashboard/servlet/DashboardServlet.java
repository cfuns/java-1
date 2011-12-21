package de.benjaminborbe.dashboard.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.CssResource;
import de.benjaminborbe.dashboard.api.CssResourceRenderer;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.dashboard.api.JavascriptResource;
import de.benjaminborbe.dashboard.api.JavascriptResourceRenderer;
import de.benjaminborbe.dashboard.api.RequireCssResource;
import de.benjaminborbe.dashboard.api.RequireJavascriptResource;
import de.benjaminborbe.dashboard.service.DashboardWidgetRegistry;

@Singleton
public class DashboardServlet extends HttpServlet {

	private final class ComparatorImplementation implements Comparator<DashboardWidget> {

		@Override
		public int compare(final DashboardWidget w1, final DashboardWidget w2) {
			return w1.getTitle().compareTo(w2.getTitle());
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final DashboardWidgetRegistry dashboardWidgetRegistry;

	private final CssResourceRenderer cssResourceRenderer;

	private final JavascriptResourceRenderer javascriptResourceRenderer;

	@Inject
	public DashboardServlet(
			final Logger logger,
			final DashboardWidgetRegistry dashboardWidgetRegistry,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final CssResourceRenderer cssResourceRenderer) {
		this.logger = logger;
		this.dashboardWidgetRegistry = dashboardWidgetRegistry;
		this.javascriptResourceRenderer = javascriptResourceRenderer;
		this.cssResourceRenderer = cssResourceRenderer;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
			IOException {
		logger.debug("service");
		response.setContentType("text/html");
		printHtml(request, response);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHead(request, response);
		printBody(request, response);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<body>");
		out.println("<h1>Dashboard</h1>");
		final List<DashboardWidget> dashboardWidgets = new ArrayList<DashboardWidget>(dashboardWidgetRegistry.getAll());
		Collections.sort(dashboardWidgets, new ComparatorImplementation());
		for (final DashboardWidget dashboardWidget : dashboardWidgets) {
			printDashboardWidget(request, response, dashboardWidget);
		}
		out.println("</body>");
	}

	protected void printDashboardWidget(final HttpServletRequest request, final HttpServletResponse response,
			final DashboardWidget dashboardWidget) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<div class=\"dashboardWidget\">");
		out.println("<h2>" + dashboardWidget.getTitle() + "</h2>");
		dashboardWidget.render(request, response);
		out.println("</div>");
	}

	protected void printHead(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		out.println("<title>Dashboard</title>");
		javascriptResourceRenderer.render(request, response, getJavascriptResources(request, response));
		cssResourceRenderer.render(request, response, getCssResources(request, response));
		out.println("</head>");
	}

	protected Collection<JavascriptResource> getJavascriptResources(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		final Set<JavascriptResource> result = new HashSet<JavascriptResource>();
		for (final DashboardWidget dashboardWidget : dashboardWidgetRegistry.getAll()) {
			if (dashboardWidget instanceof RequireJavascriptResource) {
				result.addAll(((RequireJavascriptResource) dashboardWidget).getJavascriptResource(request, response));
			}
		}
		logger.debug("found " + result + " required javascript resources");
		return result;
	}

	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		final Set<CssResource> result = new HashSet<CssResource>();
		for (final DashboardWidget dashboardWidget : dashboardWidgetRegistry.getAll()) {
			if (dashboardWidget instanceof RequireCssResource) {
				result.addAll(((RequireCssResource) dashboardWidget).getCssResource(request, response));
			}
		}
		logger.debug("found " + result + " required css resources");
		return result;
	}
}
