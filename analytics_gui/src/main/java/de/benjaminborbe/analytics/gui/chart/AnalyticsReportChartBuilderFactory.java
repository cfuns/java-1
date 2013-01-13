package de.benjaminborbe.analytics.gui.chart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.RequireJavascriptResource;

public class AnalyticsReportChartBuilderFactory implements RequireJavascriptResource {

	private final Map<AnalyticsReportChartType, AnalyticsReportChartBuilder> builders = new HashMap<AnalyticsReportChartType, AnalyticsReportChartBuilder>();

	@Inject
	public AnalyticsReportChartBuilderFactory(final AnalyticsReportChartBuilderTable table, final AnalyticsReportChartBuilderLineChart lineChart) {
		addBuilder(table);
		addBuilder(lineChart);
	}

	private void addBuilder(final AnalyticsReportChartBuilder builder) {
		builders.put(builder.getType(), builder);
	}

	public AnalyticsReportChartBuilder get(final AnalyticsReportChartType type) {
		return builders.get(type);
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		for (final AnalyticsReportChartBuilder builder : builders.values()) {
			result.addAll(builder.getJavascriptResource(request, response));
		}
		return result;
	}
}
