package de.benjaminborbe.analytics.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.analytics.gui.util.AnalyticsGuiLinkFactory;
import de.benjaminborbe.analytics.gui.util.AnalyticsReportComparator;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Singleton
public class AnalyticsGuiReportListServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Analytics - Reports";

	private final AnalyticsGuiLinkFactory analyticsGuiLinkFactory;

	private final AnalyticsService analyticsService;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	@Inject
	public AnalyticsGuiReportListServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final AnalyticsService analyticsService,
		final AnalyticsGuiLinkFactory analyticsGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.analyticsService = analyticsService;
		this.analyticsGuiLinkFactory = analyticsGuiLinkFactory;
		this.authenticationService = authenticationService;
		this.logger = logger;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<AnalyticsReport> reports = new ArrayList<>(analyticsService.getReports(sessionIdentifier));
			Collections.sort(reports, new AnalyticsReportComparator());
			final UlWidget ul = new UlWidget();
			for (final AnalyticsReport report : reports) {
				final ListWidget row = new ListWidget();
				row.add(analyticsGuiLinkFactory.reportView(request, Arrays.asList(report.getId()), AnalyticsGuiConstants.DEFAULT_INTERVAL, AnalyticsGuiConstants.DEFAULT_VIEW,
					new SpanWidget(report.getName())));
				row.add(" ");
				row.add("(" + report.getAggregation().name().toLowerCase() + ")");
				if (analyticsService.hasAnalyticsAdminPermission(sessionIdentifier)) {
					row.add(" ");
					row.add(analyticsGuiLinkFactory.reportAddData(request, report.getId()));
					row.add(" ");
					row.add(analyticsGuiLinkFactory.reportRebuild(request, report.getId()));
					row.add(" ");
					row.add(analyticsGuiLinkFactory.reportDelete(request, report.getId()));
				}
				ul.add(row);
			}
			widgets.add(ul);

			if (analyticsService.hasAnalyticsAdminPermission(sessionIdentifier)) {
				final ListWidget links = new ListWidget();
				links.add(analyticsGuiLinkFactory.addReport(request));
				links.add(" ");
				links.add(analyticsGuiLinkFactory.logWithoutReport(request));
				links.add(" ");
				links.add(analyticsGuiLinkFactory.aggregateReport(request));
				links.add(" ");
				links.add(analyticsGuiLinkFactory.reportRebuildAll(request));
				widgets.add(links);
			}

			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final AnalyticsServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			analyticsService.expectAnalyticsViewOrAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException | AnalyticsServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

}
