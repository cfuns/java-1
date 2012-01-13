package de.benjaminborbe.dashboard.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.http.HttpServletResponseBuffer;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.website.util.CssResourceImpl;

@Singleton
public class DashboardWidgetImpl implements DashboardWidget {

	private final class DashboardContentWidgetComparator implements Comparator<DashboardContentWidget> {

		@Override
		public int compare(final DashboardContentWidget widget1, final DashboardContentWidget widget2) {
			final Long p1 = new Long(widget1.getPriority());
			final Long p2 = new Long(widget2.getPriority());
			return p2.compareTo(p1);
		}
	}

	private final class DashboardWidgetRenderRunnable implements Runnable {

		private final List<StringWriter> results;

		private final HttpServletResponse response;

		private final HttpServletRequest request;

		private final DashboardContentWidget dashboardWidget;

		private final HttpContext context;

		private final CalendarUtil calendarUtil;

		private DashboardWidgetRenderRunnable(
				final HttpServletRequest request,
				final HttpServletResponse response,
				final HttpContext context,
				final DashboardContentWidget dashboardWidget,
				final List<StringWriter> results,
				final CalendarUtil calendarUtil) {
			this.context = context;
			this.results = results;
			this.response = response;
			this.request = request;
			this.dashboardWidget = dashboardWidget;
			this.calendarUtil = calendarUtil;
		}

		@Override
		public void run() {
			try {
				final long startTime = calendarUtil.getTime();
				final HttpServletResponseBuffer httpServletResponseAdapter = new HttpServletResponseBuffer(response);
				final StringWriter stringWriter = httpServletResponseAdapter.getStringWriter();
				results.add(stringWriter);
				printDashboardWidget(request, httpServletResponseAdapter, context, dashboardWidget);
				final long endTime = calendarUtil.getTime();
				stringWriter.append("<!-- render widget " + dashboardWidget.getClass().getSimpleName() + " in " + (endTime - startTime) + " ms -->");
			}
			catch (final Exception e) {
				logger.error("Render widget " + dashboardWidget.getClass().getSimpleName() + " failed!", e);
			}
		}
	}

	private final class ComparatorImplementation implements Comparator<DashboardContentWidget> {

		@Override
		public int compare(final DashboardContentWidget w1, final DashboardContentWidget w2) {
			return w1.getTitle().compareTo(w2.getTitle());
		}
	}

	private static final long serialVersionUID = 1328676176772634649L;

	private final Logger logger;

	private final DashboardWidgetRegistry dashboardWidgetRegistry;

	private final ThreadRunner threadRunner;

	private final CalendarUtil calendarUtil;

	@Inject
	public DashboardWidgetImpl(final Logger logger, final DashboardWidgetRegistry dashboardWidgetRegistry, final ThreadRunner threadRunner, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.dashboardWidgetRegistry = dashboardWidgetRegistry;
		this.threadRunner = threadRunner;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final PrintWriter out = response.getWriter();
		final List<DashboardContentWidget> dashboardWidgets = new ArrayList<DashboardContentWidget>(dashboardWidgetRegistry.getAll());
		Collections.sort(dashboardWidgets, new ComparatorImplementation());
		final Set<Thread> threads = new HashSet<Thread>();
		final List<StringWriter> results = new ArrayList<StringWriter>();
		// render all widgets
		for (final DashboardContentWidget dashboardWidget : dashboardWidgets) {
			threads.add(threadRunner.run("dashboard-widget-render", new DashboardWidgetRenderRunnable(request, response, context, dashboardWidget, results, calendarUtil)));
		}
		// wait unitil rendering finished
		for (final Thread thread : threads) {
			try {
				thread.join();
			}
			catch (final InterruptedException e) {
			}
		}
		// append output
		for (final StringWriter result : results) {
			out.println(result.toString());
		}
		out.println("<br class=\"clear\">");
	}

	protected void printDashboardWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final DashboardContentWidget dashboardWidget) throws IOException {
		final PrintWriter out = response.getWriter();
		out.println("<div class=\"dashboardWidget\">");
		out.println("<h2>" + dashboardWidget.getTitle() + "</h2>");
		dashboardWidget.render(request, response, context);
		out.println("</div>");
	}

	@Override
	public List<CssResource> getCssResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<CssResource> result = new ArrayList<CssResource>();
		result.add(new CssResourceImpl(contextPath + "/dashboard/css/style.css"));
		for (final DashboardContentWidget dashboardWidget : sortWidgets(dashboardWidgetRegistry.getAll())) {
			if (dashboardWidget instanceof RequireCssResource) {
				result.addAll(((RequireCssResource) dashboardWidget).getCssResource(request, response));
			}
		}
		logger.debug("found " + result + " required css resources");
		return result;
	}

	protected List<DashboardContentWidget> sortWidgets(final Collection<DashboardContentWidget> dashboardContentWidgets) {
		final List<DashboardContentWidget> result = new ArrayList<DashboardContentWidget>(dashboardContentWidgets);
		Collections.sort(result, new DashboardContentWidgetComparator());
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
