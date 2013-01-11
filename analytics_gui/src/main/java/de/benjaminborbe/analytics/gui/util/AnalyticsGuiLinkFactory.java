package de.benjaminborbe.analytics.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
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

	public Widget reportTable(final HttpServletRequest request, final AnalyticsReportIdentifier analyticsReportIdentifier, final String name) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_TABLE, new MapParameter().add(
				AnalyticsGuiConstants.PARAMETER_REPORT_ID, String.valueOf(analyticsReportIdentifier)), name);
	}

	public Widget reportAddData(final HttpServletRequest request, final AnalyticsReportIdentifier analyticsReportIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_ADD_DATA, new MapParameter().add(
				AnalyticsGuiConstants.PARAMETER_REPORT_ID, String.valueOf(analyticsReportIdentifier)), "add data");
	}

	public Widget addReport(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_CREATE, new MapParameter(), "add report");
	}

	public Widget reportList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_LIST, new MapParameter(), "reports");
	}

	public String reportListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_LIST;
	}
}
