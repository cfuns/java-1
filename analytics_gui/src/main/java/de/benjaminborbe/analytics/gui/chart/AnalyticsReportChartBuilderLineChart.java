package de.benjaminborbe.analytics.gui.chart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;

public class AnalyticsReportChartBuilderLineChart implements AnalyticsReportChartBuilder {

	private final ResourceUtil resourceUtil;

	@Inject
	public AnalyticsReportChartBuilderLineChart(final ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}

	@Override
	public Widget buildChart(final AnalyticsReportValueIterator reportValueIterator) throws AnalyticsServiceException, IOException {
		final ListWidget widgets = new ListWidget();
		widgets.add(new DivWidget().addId("chart"));

		widgets.add(new HtmlContentWidget(resourceUtil.getResourceContentAsString("chart_data.js")));
		widgets.add(new HtmlContentWidget(resourceUtil.getResourceContentAsString("chart_template.js")));

		return widgets;
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/" + AnalyticsGuiConstants.NAME + "/js/elycharts.min.js"));
		return result;
	}

	@Override
	public AnalyticsReportChartType getType() {
		return AnalyticsReportChartType.LINECHART;
	}

}
