package de.benjaminborbe.dashboard.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;
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

	@Inject
	public DashboardServlet(final Logger logger, final DashboardWidgetRegistry dashboardWidgetRegistry) {
		this.logger = logger;
		this.dashboardWidgetRegistry = dashboardWidgetRegistry;
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
		out.println("</head>");
	}
}
