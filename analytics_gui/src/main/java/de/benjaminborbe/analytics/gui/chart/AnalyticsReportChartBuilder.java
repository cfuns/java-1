package de.benjaminborbe.analytics.gui.chart;

import java.io.IOException;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;

public interface AnalyticsReportChartBuilder extends RequireJavascriptResource {

	AnalyticsReportChartType getType();

	Widget buildChart(SessionIdentifier sessionIdentifier, AnalyticsReportIdentifier reportIdentifier, AnalyticsReportInterval selectedAnalyticsReportInterval)
			throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException, IOException;
}
