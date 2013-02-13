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
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
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
		final AnalyticsReportValueIterator reportValueIterator = analyticsService.getReportIteratorFillMissing(sessionIdentifier, reportIdentifiers.get(0),
				selectedAnalyticsReportInterval);
		final ListWidget widgets = new ListWidget();
		widgets.add(new DivWidget().addId("chart"));

		final List<String> tooltips = new ArrayList<String>();
		final List<String> values = new ArrayList<String>();
		final DecimalFormat df = new DecimalFormat("#####0.0");

		int counter = 0;
		while (reportValueIterator.hasNext() && counter < LIMIT) {
			counter++;
			final AnalyticsReportValue reportValue = reportValueIterator.next();
			tooltips.add(calendarUtil.toDateTimeString(reportValue.getDate()));
			values.add(reportValue.getValue() != null ? df.format(reportValue.getValue()) : null);
		}
		Collections.reverse(tooltips);
		Collections.reverse(values);
		widgets.add(new JavascriptWidget(buildContent(tooltips, values)));
		widgets.add(new JavascriptWidget(resourceUtil.getResourceContentAsString("chart_template.js")));

		return widgets;
	}

	private String buildContent(final List<String> tooltips, final List<String> values) throws IOException {
		final String content = resourceUtil.getResourceContentAsString("chart_data.js");
		final String tooltipString = buildTooltips(tooltips, values);
		final String valuesString = StringUtils.join(values, ", ");
		return content.replace("{values}", valuesString).replace("{tooltips}", tooltipString);
	}

	private String buildTooltips(final List<String> tooltips, final List<String> values) {
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
