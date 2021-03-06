package de.benjaminborbe.analytics.gui.chart;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.api.IteratorWithException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.iterator.IteratorByListReverse;
import de.benjaminborbe.tools.iterator.IteratorException;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.JavascriptWidget;
import de.benjaminborbe.website.util.ListWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsReportChartBuilderLineChart implements AnalyticsReportChartBuilder {

	private static final int LIMIT = 60;

	private final ResourceUtil resourceUtil;

	private final CalendarUtil calendarUtil;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportChartColorGenerator analyticsReportChartColorGenerator;

	private final AnalyticsReportLabelBuilder analyticsReportLabelBuilder;

	@Inject
	public AnalyticsReportChartBuilderLineChart(
		final AnalyticsService analyticsService,
		final ResourceUtil resourceUtil,
		final CalendarUtil calendarUtil,
		final AnalyticsReportChartColorGenerator analyticsReportChartColorGenerator,
		final AnalyticsReportLabelBuilder analyticsReportLabelBuilder
	) {
		this.analyticsService = analyticsService;
		this.resourceUtil = resourceUtil;
		this.calendarUtil = calendarUtil;
		this.analyticsReportChartColorGenerator = analyticsReportChartColorGenerator;
		this.analyticsReportLabelBuilder = analyticsReportLabelBuilder;
	}

	@Override
	public Widget buildChart(
		final SessionIdentifier sessionIdentifier, final List<AnalyticsReportIdentifier> reportIdentifiers,
		final AnalyticsReportInterval selectedAnalyticsReportInterval
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException, IOException {
		try {
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

					final String key;
					if (reportValue != null && reportValue.getDate() != null) {
						key = calendarUtil.toDateTimeString(reportValue.getDate());
					} else {
						key = null;
					}
					tooltips.get(i).add(key);

					final String value;
					if (reportValue != null && reportValue.getValue() != null) {
						value = df.format(reportValue.getValue());
					} else {
						value = null;
					}
					values.get(i).add(value);
				}
			}
			widgets.add(new JavascriptWidget(buildContent(tooltips, values, reportIdentifiers)));
			widgets.add(new JavascriptWidget(buildTemplate(reportIdentifiers)));

			return widgets;
		} catch (final IteratorException e) {
			throw new AnalyticsServiceException(e);
		} finally {
		}
	}

	private String buildTemplate(final List<AnalyticsReportIdentifier> reportIdentifiers) throws IOException {
		final String content = resourceUtil.getResourceContentAsString("chart_template.js");
		final List<String> c = analyticsReportChartColorGenerator.getColors(reportIdentifiers.size());
		final StringBuilder colors = new StringBuilder();
		for (int i = 0; i < Math.min(reportIdentifiers.size(), c.size()); ++i) {
			colors.append("serie" + (i + 1) + ": {");
			colors.append("color: \"" + c.get(i) + "\"");
			colors.append("},\n");
		}
		return content.replace("{colors}", colors);
	}

	private String buildContent(
		final List<List<String>> tooltips,
		final List<List<String>> values,
		final List<AnalyticsReportIdentifier> reportIdentifiers
	) throws IOException,
		IteratorException, AnalyticsServiceException {
		final String content = resourceUtil.getResourceContentAsString("chart_data.js");
		final String tooltipString = buildTooltips(tooltips, values, reportIdentifiers);
		final String valuesString = buildValues(values);
		final String legendString = buildLegend(reportIdentifiers);
		return content.replace("{values}", valuesString).replace("{tooltips}", tooltipString).replace("{legend}", legendString);
	}

	private String buildLegend(final List<AnalyticsReportIdentifier> reportIdentifiers) throws AnalyticsServiceException {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < reportIdentifiers.size(); ++i) {
			final AnalyticsReportIdentifier reportIdentifier = reportIdentifiers.get(i);
			final AnalyticsReport analyticsReport = analyticsService.getReport(reportIdentifier);
			sb.append("serie" + (i + 1) + ": \"" + analyticsReportLabelBuilder.createLabel(analyticsReport) + "\"");
			sb.append(",\n");
		}
		return sb.toString();
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
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int i = values.size() - 1; i >= 0; --i) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(values.get(i));
		}
		return sb.toString();
	}

	private String buildTooltips(final List<List<String>> tooltips, final List<List<String>> values, final List<AnalyticsReportIdentifier> reportIdentifiers)
		throws IteratorException {
		final StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= values.size(); ++i) {
			sb.append("serie" + i + ": [");
			sb.append(buildTooltip(tooltips.get(i - 1), values.get(i - 1), reportIdentifiers.get(i - 1)));
			sb.append("],\n");
		}
		return sb.toString();
	}

	private String buildTooltip(
		final List<String> tooltips,
		final List<String> values,
		final AnalyticsReportIdentifier analyticsReportIdentifier
	) throws IteratorException {
		if (tooltips.isEmpty()) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();

		final IteratorWithException<String, IteratorException> ti = new IteratorByListReverse<String, IteratorException>(tooltips);
		final IteratorWithException<String, IteratorException> vi = new IteratorByListReverse<String, IteratorException>(values);
		boolean first = true;
		while (ti.hasNext() && vi.hasNext()) {
			final String tooltip = ti.next();
			final String value = vi.next();
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}

			sb.append('"');
			sb.append(analyticsReportIdentifier.getId());
			sb.append("<br>");
			sb.append(value);
			sb.append("<br>");
			sb.append(tooltip);
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
