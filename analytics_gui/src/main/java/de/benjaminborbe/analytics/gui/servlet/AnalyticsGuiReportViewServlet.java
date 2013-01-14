package de.benjaminborbe.analytics.gui.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
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
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;

@Singleton
public class AnalyticsGuiReportViewServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Analytics - Report view";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ParseUtil parseUtil;

	private final AnalyticsGuiLinkFactory analyticsGuiLinkFactory;

	private final AnalyticsReportChartBuilderFactory chartBuilderFactory;

	@Inject
	public AnalyticsGuiReportViewServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final AnalyticsGuiLinkFactory analyticsGuiLinkFactory,
			final AnalyticsReportChartBuilderFactory chartBuilderFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.parseUtil = parseUtil;
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.analyticsGuiLinkFactory = analyticsGuiLinkFactory;
		this.chartBuilderFactory = chartBuilderFactory;
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
			final AnalyticsReportIdentifier reportIdentifier = new AnalyticsReportIdentifier(request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_ID));
			final AnalyticsReportInterval selectedAnalyticsReportInterval = parseUtil.parseEnum(AnalyticsReportInterval.class,
					request.getParameter(AnalyticsGuiConstants.PARAMETER_REPORT_INTERVAL), AnalyticsGuiConstants.DEFAULT_INTERVAL);
			final AnalyticsReportChartType selectedChartType = parseUtil.parseEnum(AnalyticsReportChartType.class, request.getParameter(AnalyticsGuiConstants.PARAMETER_CHART_TYPE),
					AnalyticsGuiConstants.DEFAULT_VIEW);

			widgets.add(new H1Widget(getTitle() + " - " + reportIdentifier));

			// interval
			{
				widgets.add(new H2Widget("Interval:"));
				for (final AnalyticsReportInterval analyticsReportInterval : AnalyticsReportInterval.values()) {
					final ListWidget list = new ListWidget();
					final SpanWidget name = new SpanWidget(analyticsReportInterval.name().toLowerCase());
					if (analyticsReportInterval.equals(selectedAnalyticsReportInterval)) {
						name.addClass("selected");
					}
					list.add(analyticsGuiLinkFactory.reportView(request, reportIdentifier, analyticsReportInterval, selectedChartType, name));
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
					list.add(analyticsGuiLinkFactory.reportView(request, reportIdentifier, selectedAnalyticsReportInterval, chartType, name));
					list.add(" ");
					widgets.add(list);
				}
			}

			// chart
			{
				final AnalyticsReportChartBuilder builder = chartBuilderFactory.get(selectedChartType);
				if (builder == null) {
					widgets.add("no chart found for type: " + selectedChartType);
				}
				else {
					widgets.add(builder.buildChart(sessionIdentifier, reportIdentifier, selectedAnalyticsReportInterval));
				}
			}

			return widgets;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AnalyticsServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
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
}
