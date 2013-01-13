package de.benjaminborbe.analytics.gui.chart;

import java.io.IOException;

import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.html.api.RequireJavascriptResource;
import de.benjaminborbe.html.api.Widget;

public interface AnalyticsReportChartBuilder extends RequireJavascriptResource {

	Widget buildChart(AnalyticsReportValueIterator reportValueIterator) throws AnalyticsServiceException, IOException;

	AnalyticsReportChartType getType();
}
