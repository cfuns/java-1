package de.benjaminborbe.dashboard.gui.service;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.api.DashboardIdentifier;
import de.benjaminborbe.dashboard.api.DashboardService;
import de.benjaminborbe.dashboard.api.DashboardServiceException;
import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.dashboard.gui.util.DashboardGuiContentWidgetComparator;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireCssResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.http.HttpServletResponseBuffer;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ClearFloatWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class DashboardGuiWidgetImpl extends CompositeWidget implements DashboardWidget {

	private final class SelectedPredicate implements Predicate<DashboardContentWidget> {

		private final Collection<DashboardIdentifier> selected;

		public SelectedPredicate(final Collection<DashboardIdentifier> selected) {
			this.selected = selected;
		}

		@Override
		public boolean apply(final DashboardContentWidget widget) {
			return selected.contains(new DashboardIdentifier(widget.getTitle()));
		}
	}

	private final class HasAdminPredicate implements Predicate<DashboardContentWidget> {

		private final boolean hasAdmin;

		private HasAdminPredicate(final boolean hasAdmin) {
			this.hasAdmin = hasAdmin;
		}

		@Override
		public boolean apply(final DashboardContentWidget widget) {
			if (widget.isAdminRequired() && !hasAdmin) {
				return false;
			} else {
			}
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
			final CalendarUtil calendarUtil
		) {
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
				final Widget widget = createDashboardWidget(dashboardWidget);
				widget.render(request, httpServletResponseAdapter, context);
				final long endTime = calendarUtil.getTime();
				stringWriter.append("<!-- render widget " + dashboardWidget.getClass().getSimpleName() + " in " + (endTime - startTime) + " ms -->");
				threadResult.set(stringWriter.toString());
			} catch (final Exception e) {
				logger.error("Render widget " + dashboardWidget.getClass().getSimpleName() + " failed!", e);
			}
		}
	}

	private final Logger logger;

	private final DashboardGuiWidgetRegistry dashboardWidgetRegistry;

	private final ThreadRunner threadRunner;

	private final CalendarUtil calendarUtil;

	private final AuthorizationService authorizationService;

	private final AuthenticationService authenticationService;

	private final DashboardGuiContentWidgetComparator dashboardContentWidgetComparator;

	private final DashboardService dashboardService;

	@Inject
	public DashboardGuiWidgetImpl(
		final Logger logger,
		final DashboardService dashboardService,
		final DashboardGuiContentWidgetComparator dashboardContentWidgetComparator,
		final DashboardGuiWidgetRegistry dashboardWidgetRegistry,
		final ThreadRunner threadRunner,
		final CalendarUtil calendarUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService
	) {
		this.logger = logger;
		this.dashboardService = dashboardService;
		this.dashboardContentWidgetComparator = dashboardContentWidgetComparator;
		this.dashboardWidgetRegistry = dashboardWidgetRegistry;
		this.threadRunner = threadRunner;
		this.calendarUtil = calendarUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
	}

	private Collection<DashboardContentWidget> getWidgets(final SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, DashboardServiceException,
		LoginRequiredException, PermissionDeniedException {
		final boolean hasAdmin = authorizationService.hasAdminRole(sessionIdentifier);
		final List<DashboardContentWidget> dashboardWidgets = new ArrayList<DashboardContentWidget>(dashboardWidgetRegistry.getAll());
		Collections.sort(dashboardWidgets, dashboardContentWidgetComparator);

		final Collection<DashboardIdentifier> selected = dashboardService.getSelectedDashboards(sessionIdentifier);
		return Collections2.filter(dashboardWidgets, Predicates.and(new HasAdminPredicate(hasAdmin), new SelectedPredicate(selected)));
	}

	protected Widget createDashboardWidget(final DashboardContentWidget dashboardWidget) throws IOException, PermissionDeniedException {
		final DivWidget div = new DivWidget().addClass("dashboardWidget");
		if (dashboardWidget.getName() != null) {
			div.addClass(dashboardWidget.getName() + "DashboardWidget");
		}
		final ListWidget content = new ListWidget();
		content.add(new H2Widget(dashboardWidget.getTitle()));
		content.add(dashboardWidget);
		div.addContent(content);
		return div;
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

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		try {
			final ListWidget widgets = new ListWidget();
			final Set<Thread> threads = new HashSet<Thread>();
			final List<ThreadResult<String>> results = new ArrayList<ThreadResult<String>>();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			// render all widgets
			for (final DashboardContentWidget dashboardWidget : getWidgets(sessionIdentifier)) {
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
				} catch (final InterruptedException e) {
				}
			}

			// warn not finished threads
			for (final Thread thread : threads) {
				if (thread.isAlive()) {
					logger.warn("thread " + thread.getName() + " not finish in " + (timeout / 1000) + " seconds");
					try {
						thread.stop();
					} catch (final Exception e) {
					}
				}
			}

			// append output
			for (final ThreadResult<String> result : results) {
				final String content = result.get();
				if (content != null) {
					widgets.add(new HtmlContentWidget(content));
				} else {
					logger.trace("no content found");
				}
			}
			widgets.add(new ClearFloatWidget());
			return widgets;
		} catch (final AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		} catch (final AuthorizationServiceException e) {
			return new ExceptionWidget(e);
		} catch (final DashboardServiceException e) {
			return new ExceptionWidget(e);
		} catch (final LoginRequiredException e) {
			return new ExceptionWidget(e);
		}
	}

}
