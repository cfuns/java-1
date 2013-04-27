package de.benjaminborbe.analytics.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.analytics.gui.chart.AnalyticsReportChartBuilder;
import de.benjaminborbe.analytics.gui.chart.AnalyticsReportChartBuilderFactory;
import de.benjaminborbe.analytics.gui.chart.AnalyticsReportChartType;
import de.benjaminborbe.analytics.gui.util.AnalyticsGuiLinkFactory;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class AnalyticsGuiReportViewServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Analytics - Report view";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final AnalyticsGuiLinkFactory analyticsGuiLinkFactory;

	private final AnalyticsReportChartBuilderFactory chartBuilderFactory;

	private final AnalyticsService analyticsService;

	@Inject
	public AnalyticsGuiReportViewServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final AnalyticsGuiLinkFactory analyticsGuiLinkFactory,
		final AnalyticsReportChartBuilderFactory chartBuilderFactory,
		final AnalyticsService analyticsService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.parseUtil = parseUtil;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.analyticsGuiLinkFactory = analyticsGuiLinkFactory;
		this.chartBuilderFactory = chartBuilderFactory;
		this.analyticsService = analyticsService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);

			final List<AnalyticsReportIdentifier> reportIdentifiers = buildReportIdentifiers(request.getParameterValues(AnalyticsGuiConstants.PARAMETER_REPORT_ID));

			final AnalyticsReportInterval selectedAnalyticsReportInterval = parseUtil.parseEnum(AnalyticsReportInterval.class,
				request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_INTERVAL), AnalyticsGuiConstants.DEFAULT_INTERVAL);
			final AnalyticsReportChartType selectedChartType = parseUtil.parseEnum(AnalyticsReportChartType.class, request.getParameter(AnalyticsGuiConstants.PARAMETER_CHART_TYPE),
				AnalyticsGuiConstants.DEFAULT_VIEW);

			widgets.add(new H1Widget(buildTitle(reportIdentifiers)));

			// interval
			{
				widgets.add(new H2Widget("Interval:"));
				for (final AnalyticsReportInterval analyticsReportInterval : AnalyticsReportInterval.values()) {
					final ListWidget list = new ListWidget();
					final SpanWidget name = new SpanWidget(analyticsReportInterval.name().toLowerCase());
					if (analyticsReportInterval.equals(selectedAnalyticsReportInterval)) {
						name.addClass("selected");
					}
					list.add(analyticsGuiLinkFactory.reportView(request, reportIdentifiers, analyticsReportInterval, selectedChartType, name));
					list.add(" ");
					widgets.add(list);
				}
			}

			// chart type
			{
				widgets.add(new H2Widget("ChartType:"));
				for (final AnalyticsReportChartType chartType : AnalyticsReportChartType.values()) {
					final ListWidget list = new ListWidget();
					final SpanWidget name = new SpanWidget(chartType.name().toLowerCase());
					if (selectedChartType.equals(chartType)) {
						name.addClass("selected");
					}
					list.add(analyticsGuiLinkFactory.reportView(request, reportIdentifiers, selectedAnalyticsReportInterval, chartType, name));
					list.add(" ");
					widgets.add(list);
				}
			}

			// chart
			{
				final AnalyticsReportChartBuilder builder = chartBuilderFactory.get(selectedChartType);
				if (builder == null) {
					widgets.add("no chart found for type: " + selectedChartType);
				} else {
					widgets.add(builder.buildChart(sessionIdentifier, reportIdentifiers, selectedAnalyticsReportInterval));
				}
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

	private Widget buildTitle(final List<AnalyticsReportIdentifier> reportIdentifiers) {
		final ListWidget widgets = new ListWidget();
		widgets.add(getTitle());
		widgets.add(" - ");
		boolean first = true;
		for (final AnalyticsReportIdentifier reportIdentifier : reportIdentifiers) {
			if (first) {
				first = false;
			} else {
				widgets.add(", ");
			}
			widgets.add(reportIdentifier.getId());
		}
		return widgets;
	}

	private List<AnalyticsReportIdentifier> buildReportIdentifiers(final String[] reportIds) {
		final List<AnalyticsReportIdentifier> result = new ArrayList<>();
		if (reportIds != null) {
			for (final String reportId : reportIds) {
				result.add(new AnalyticsReportIdentifier(reportId));
			}
		}
		return result;
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		return chartBuilderFactory.getJavascriptResource(request, response);
	}

	@Override
	protected Collection<CssResource> getCssResources(final HttpServletRequest request, final HttpServletResponse response) {
		final Collection<CssResource> result = super.getCssResources(request, response);
		result.add(new CssResourceImpl(request.getContextPath() + "/" + AnalyticsGuiConstants.NAME + "/css/style.css"));
		return result;
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
