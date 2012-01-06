package de.benjaminborbe.worktime.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.JavascriptResourceRenderer;
import de.benjaminborbe.worktime.service.WorktimeDashboardWidget;

@Singleton
public class WorktimeServlet extends HttpServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final WorktimeDashboardWidget worktimeDashboardWidget;

	private final JavascriptResourceRenderer javascriptResourceRenderer;

	@Inject
	public WorktimeServlet(final Logger logger, final WorktimeDashboardWidget worktimeDashboardWidget, final JavascriptResourceRenderer javascriptResourceRenderer) {
		this.logger = logger;
		this.worktimeDashboardWidget = worktimeDashboardWidget;
		this.javascriptResourceRenderer = javascriptResourceRenderer;
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		logger.debug("service");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		printHtml(request, response);
	}

	protected void printHtml(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final PrintWriter out = response.getWriter();
		out.println("<html>");
		printHeader(request, response);
		printBody(request, response);
		out.println("</html>");
	}

	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<body>");
		out.println("<h1>Worktime</h1>");
		worktimeDashboardWidget.render(request, response);
		out.println("</body>");
	}

	protected void printHeader(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<head>");
		javascriptResourceRenderer.render(request, response, worktimeDashboardWidget.getJavascriptResource(request, response));
		out.println("</head>");
	}
}
