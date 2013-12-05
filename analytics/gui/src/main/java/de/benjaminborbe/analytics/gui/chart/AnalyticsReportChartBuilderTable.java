package de.benjaminborbe.analytics.gui.chart;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsReportInterval;
import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueListIterator;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.website.table.TableCellHeadWidget;
import de.benjaminborbe.website.table.TableCellWidget;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsReportChartBuilderTable implements AnalyticsReportChartBuilder {

	private static final int LIMIT = 100;

	private final CalendarUtil calendarUtil;

	private final AnalyticsReportLabelBuilder analyticsReportLabelBuilder;

	private final AnalyticsService analyticsService;

	@Inject
	public AnalyticsReportChartBuilderTable(
		final AnalyticsService analyticsService,
		final CalendarUtil calendarUtil,
		final AnalyticsReportLabelBuilder analyticsReportLabelBuilder
	) {
		this.analyticsService = analyticsService;
		this.calendarUtil = calendarUtil;
		this.analyticsReportLabelBuilder = analyticsReportLabelBuilder;
	}

	@Override
	public Widget buildChart(
		final SessionIdentifier sessionIdentifier, final List<AnalyticsReportIdentifier> reportIdentifiers,
		final AnalyticsReportInterval selectedAnalyticsReportInterval
	) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final AnalyticsReportValueListIterator reportValueIterator = analyticsService.getReportListIterator(sessionIdentifier, reportIdentifiers, selectedAnalyticsReportInterval);
		final DecimalFormat df = new DecimalFormat("#####0.0");

		final TableWidget table = new TableWidget();
		table.addClass("sortable");
		{
			final TableRowWidget row = new TableRowWidget();
			row.addCell(new TableCellHeadWidget("Name"));
			for (final AnalyticsReportIdentifier reportIdentifier : reportIdentifiers) {
				final AnalyticsReport analyticsReport = analyticsService.getReport(reportIdentifier);
				row.addCell(new TableCellHeadWidget(analyticsReportLabelBuilder.createLabel(analyticsReport)));
			}
			table.addRow(row);
		}

		int counter = 0;
		while (reportValueIterator.hasNext() && counter < LIMIT) {
			counter++;
			final List<AnalyticsReportValue> reportValues = reportValueIterator.next();
			final TableRowWidget row = new TableRowWidget();
			{
				final AnalyticsReportValue analyticsReportValue = reportValues.get(0);
				final String value;
				if (analyticsReportValue != null) {
					value = calendarUtil.toDateTimeString(analyticsReportValue.getDate());
				} else {
					value = null;
				}
				row.addCell(value);
			}
			for (final AnalyticsReportValue reportValue : reportValues) {
				final Double reportValueValue;
				if (reportValue != null) {
					reportValueValue = reportValue.getValue();
				} else {
					reportValueValue = null;
				}
				final String value = reportValueValue != null ? df.format(reportValueValue) : null;
				final TableCellWidget cell = new TableCellWidget(value);
				cell.addAttribute("sorttable_customkey", value);
				row.addCell(cell);
			}
			table.addRow(row);
		}
		return table;
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	public AnalyticsReportChartType getType() {
		return AnalyticsReportChartType.TABLE;
	}

}
