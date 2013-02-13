package de.benjaminborbe.analytics.gui.chart;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.JavascriptWidget;
import de.benjaminborbe.website.util.ListWidget;

public class AnalyticsReportChartBuilderLineChart implements AnalyticsReportChartBuilder {

	private static final int LIMIT = 60;

	private final ResourceUtil resourceUtil;

	private final CalendarUtil calendarUtil;

	private final AnalyticsService analyticsService;

	@Inject
	public AnalyticsReportChartBuilderLineChart(final AnalyticsService analyticsService, final ResourceUtil resourceUtil, final CalendarUtil calendarUtil) {
		this.analyticsService = analyticsService;
		this.resourceUtil = resourceUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Widget buildChart(final SessionIdentifier sessionIdentifier, final List<AnalyticsReportIdentifier> reportIdentifiers,
			final AnalyticsReportInterval selectedAnalyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException, IOException {
		final AnalyticsReportValueListIterator reportValueIterator = analyticsService.getReportListIteratorFillMissing(sessionIdentifier, reportIdentifiers,
				selectedAnalyticsReportInterval);
		final ListWidget widgets = new ListWidget();
		widgets.add(new DivWidget().addId("chart"));

		final List<List<String>> tooltips = new ArrayList<List<String>>();
		final List<List<String>> values = new ArrayList<List<String>>();
		final DecimalFormat df = new DecimalFormat("#####0.0");

		for (int i = 0; i < reportIdentifiers.size(); ++i) {
			tooltips.add(new ArrayList<String>());
			values.add(new ArrayList<String>());
		}

		int counter = 0;
		while (reportValueIterator.hasNext() && counter < LIMIT) {
			counter++;
			final List<AnalyticsReportValue> reportValues = reportValueIterator.next();
			for (int i = 0; i < reportValues.size(); ++i) {
				final AnalyticsReportValue reportValue = reportValues.get(i);
				tooltips.get(i).add(calendarUtil.toDateTimeString(reportValue.getDate()));
				values.get(i).add(reportValue.getValue() != null ? df.format(reportValue.getValue()) : null);
			}
		}
		Collections.reverse(tooltips);
		Collections.reverse(values);
		widgets.add(new JavascriptWidget(buildContent(tooltips, values)));
		widgets.add(new JavascriptWidget(resourceUtil.getResourceContentAsString("chart_template.js")));

		return widgets;
	}

	private String buildContent(final List<List<String>> tooltips, final List<List<String>> values) throws IOException {
		final String content = resourceUtil.getResourceContentAsString("chart_data.js");
		final String tooltipString = buildTooltips(tooltips, values);
		final String valuesString = buildValues(values);
		return content.replace("{values}", valuesString).replace("{tooltips}", tooltipString);
	}

	private String buildValues(final List<List<String>> values) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= values.size(); ++i) {
			sb.append("serie" + i + ": [");
			sb.append(buildValue(values.get(i - 1)));
			sb.append("],\n");
		}
		return sb.toString();
	}

	private String buildValue(final List<String> values) {
		return StringUtils.join(values, ", ");
	}

	private String buildTooltips(final List<List<String>> tooltips, final List<List<String>> values) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= values.size(); ++i) {
			sb.append("serie" + i + ": [");
			sb.append(buildTooltip(tooltips.get(i - 1), values.get(i - 1)));
			sb.append("],\n");
		}
		return sb.toString();
	}

	private String buildTooltip(final List<String> tooltips, final List<String> values) {
		if (tooltips.isEmpty()) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();

		final Iterator<String> ti = tooltips.iterator();
		final Iterator<String> vi = values.iterator();
		boolean first = true;
		while (ti.hasNext() && vi.hasNext()) {
			final String tooltip = ti.next();
			final String value = vi.next();
			if (first) {
				first = false;
			}
			else {
				sb.append(", ");
			}

			final String[] parts = tooltip.split(" ");
			sb.append('"');
			sb.append(value);
			sb.append("<br>");
			sb.append(parts[0]);
			sb.append("<br>");
			sb.append(parts[1]);
			sb.append('"');
		}
		return sb.toString();
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(request.getScheme() + "://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"));
		result.add(new JavascriptResourceImpl(contextPath + "/" + AnalyticsGuiConstants.NAME + "/js/raphael.js"));
		result.add(new JavascriptResourceImpl(contextPath + "/" + AnalyticsGuiConstants.NAME + "/js/elycharts.min.js"));
		return result;
	}

	@Override
	public AnalyticsReportChartType getType() {
		return AnalyticsReportChartType.LINECHART;
	}

}
