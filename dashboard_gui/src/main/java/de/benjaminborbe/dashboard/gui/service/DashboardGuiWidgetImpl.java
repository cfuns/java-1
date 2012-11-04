package de.benjaminborbe.dashboard.gui.service;

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

import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.http.HttpServletResponseBuffer;
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.website.util.CssResourceImpl;

@Singleton
public class DashboardGuiWidgetImpl implements DashboardWidget {

	private final class DashboardContentWidgetComparator extends ComparatorBase<DashboardContentWidget, Long> {

		@Override
		public Long getValue(final DashboardContentWidget o) {
			return new Long(o.getPriority());
		}

		@Override
		public boolean inverted() {
			return true;
		}

	}

	private final class DashboardWidgetRenderRunnable implements Runnable {

		private final HttpServletResponse response;

		private final HttpServletRequest request;

		private final DashboardContentWidget dashboardWidget;

		private final HttpContext context;

		private final CalendarUtil calendarUtil;

		private final ThreadResult<String> threadResult;

		private DashboardWidgetRenderRunnable(
				final HttpServletRequest request,
				final HttpServletResponse response,
				final HttpContext context,
				final DashboardContentWidget dashboardWidget,
				final ThreadResult<String> threadResult,
				final CalendarUtil calendarUtil) {
			this.context = context;
			this.threadResult = threadResult;
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
				printDashboardWidget(request, httpServletResponseAdapter, context, dashboardWidget);
				final long endTime = calendarUtil.getTime();
				stringWriter.append("<!-- render widget " + dashboardWidget.getClass().getSimpleName() + " in " + (endTime - startTime) + " ms -->");
				threadResult.set(stringWriter.toString());
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

	private final Logger logger;

	private final DashboardGuiWidgetRegistry dashboardWidgetRegistry;

	private final ThreadRunner threadRunner;

	private final CalendarUtil calendarUtil;

	@Inject
	public DashboardGuiWidgetImpl(final Logger logger, final DashboardGuiWidgetRegistry dashboardWidgetRegistry, final ThreadRunner threadRunner, final CalendarUtil calendarUtil) {
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
		final List<ThreadResult<String>> results = new ArrayList<ThreadResult<String>>();
		// render all widgets
		for (final DashboardContentWidget dashboardWidget : sortWidgets(dashboardWidgets)) {
			final ThreadResult<String> result = new ThreadResult<String>();
			results.add(result);
			threads.add(threadRunner.run("dashboard-widget-render " + dashboardWidget.getClass().getSimpleName(), new DashboardWidgetRenderRunnable(request, response, context,
					dashboardWidget, result, calendarUtil)));
		}
		// wait 1 second
		final long timeout = 1000;

		// wait unitil rendering finished
		for (final Thread thread : threads) {
			try {
				thread.join(timeout);
			}
			catch (final InterruptedException e) {
			}
		}

		// warn not finished threads
		for (final Thread thread : threads) {
			if (thread.isAlive()) {
				logger.warn("thread " + thread.getName() + " not finish in " + (timeout / 1000) + " seconds");
				try {
					thread.stop();
				}
				catch (final Exception e) {
				}
			}
		}

		// append output
		for (final ThreadResult<String> result : results) {
			final String content = result.get();
			if (content != null) {
				out.println(content);
			}
			else {
				logger.trace("no content found");
			}
		}
		out.println("<br class=\"clear\">");
	}

	protected void printDashboardWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context, final DashboardContentWidget dashboardWidget)
			throws IOException, PermissionDeniedException {
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
		for (final DashboardContentWidget dashboardWidget : dashboardWidgetRegistry.getAll()) {
			if (dashboardWidget instanceof RequireCssResource) {
				result.addAll(((RequireCssResource) dashboardWidget).getCssResource(request, response));
			}
		}
		logger.trace("found " + result + " required css resources");
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
		logger.trace("found " + result + " required javascript resources");
		return result;
	}

}
