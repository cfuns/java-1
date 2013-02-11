package de.benjaminborbe.analytics.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.analytics.gui.chart.AnalyticsReportChartType;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class AnalyticsGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public AnalyticsGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget reportView(final HttpServletRequest request, final AnalyticsReportIdentifier analyticsReportIdentifier, final AnalyticsReportInterval analyticsReportInterval,
			final AnalyticsReportChartType type, final Widget widget) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_VIEW, new MapParameter()
				.add(AnalyticsGuiConstants.PARAMETER_REPORT_ID, String.valueOf(analyticsReportIdentifier))
				.add(AnalyticsGuiConstants.PARAMETER_REPORT_INTERVAL, String.valueOf(analyticsReportInterval)).add(AnalyticsGuiConstants.PARAMETER_CHART_TYPE, String.valueOf(type)),
				widget);
	}

	public Widget reportAddData(final HttpServletRequest request, final AnalyticsReportIdentifier analyticsReportIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_ADD_DATA, new MapParameter().add(
				AnalyticsGuiConstants.PARAMETER_REPORT_ID, String.valueOf(analyticsReportIdentifier)), "insert");
	}

	public Widget addReport(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_CREATE, new MapParameter(), "add report");
	}

	public Widget addReport(final HttpServletRequest request, final String name) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_CREATE, new MapParameter().add(
				AnalyticsGuiConstants.PARAMETER_REPORT_NAME, name), "add report");
	}

	public Widget reportList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_LIST, new MapParameter(), "reports");
	}

	public String reportListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_LIST;
	}

	public Widget reportDelete(final HttpServletRequest request, final AnalyticsReportIdentifier analyticsReportIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_DELETE, new MapParameter().add(
				AnalyticsGuiConstants.PARAMETER_REPORT_ID, String.valueOf(analyticsReportIdentifier)), "delete").addConfirm("delete report?");
	}

	public Widget aggregateReport(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_AGGREGATE, new MapParameter(), "aggregate")
				.addConfirm("aggregate?");
	}

	public Widget logWithoutReport(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_LOG_WITHOUT_REPORT, new MapParameter(), "log without report");
	}

}
