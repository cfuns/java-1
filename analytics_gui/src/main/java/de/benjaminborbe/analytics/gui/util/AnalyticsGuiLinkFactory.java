package de.benjaminborbe.analytics.gui.util;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class AnalyticsGuiLinkFactory {

	@Inject
	public AnalyticsGuiLinkFactory() {
	}

	public Widget tableReport(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_REPORT_TABLE, "report");
	}

	public Widget addData(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + AnalyticsGuiConstants.NAME + AnalyticsGuiConstants.URL_ADD_DATA, "add data");
	}
}
