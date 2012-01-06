package de.benjaminborbe.worktime.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.worktime.service.WorktimeDashboardWidget;

@Singleton
public class WorktimeServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Worktime";

	private final WorktimeDashboardWidget worktimeDashboardWidget;

	@Inject
	public WorktimeServlet(
			final Logger logger,
			final CssResourceRenderer cssResourceRenderer,
			final JavascriptResourceRenderer javascriptResourceRenderer,
			final WorktimeDashboardWidget worktimeDashboardWidget) {
		super(logger, cssResourceRenderer, javascriptResourceRenderer);
		this.worktimeDashboardWidget = worktimeDashboardWidget;
	}

	@Override
	protected void printBody(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<body>");
		out.println("<h1>Worktime</h1>");
		worktimeDashboardWidget.render(request, response);
		out.println("</body>");
	}

	@Override
	protected Collection<Widget> getWidgets() {
		final Set<Widget> result = new HashSet<Widget>();
		result.add(worktimeDashboardWidget);
		return result;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}
}
