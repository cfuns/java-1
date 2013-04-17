package de.benjaminborbe.analytics.gui.chart;

import com.google.inject.Inject;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsReportChartBuilderTable implements AnalyticsReportChartBuilder {

	private static final int LIMIT = 100;

	private final CalendarUtil calendarUtil;

	private final AnalyticsService analyticsService;

	@Inject
	public AnalyticsReportChartBuilderTable(final AnalyticsService analyticsService, final CalendarUtil calendarUtil) {
		this.analyticsService = analyticsService;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Widget buildChart(final SessionIdentifier sessionIdentifier, final List<AnalyticsReportIdentifier> reportIdentifiers,
													 final AnalyticsReportInterval selectedAnalyticsReportInterval) throws AnalyticsServiceException, PermissionDeniedException, LoginRequiredException {
		final AnalyticsReportValueListIterator reportValueIterator = analyticsService.getReportListIterator(sessionIdentifier, reportIdentifiers, selectedAnalyticsReportInterval);
		final DecimalFormat df = new DecimalFormat("#####0.0");

		final TableWidget table = new TableWidget();
		table.addClass("sortable");
		{
			final TableRowWidget row = new TableRowWidget();
			row.addCell(new TableCellHeadWidget("Name"));
			for (final AnalyticsReportIdentifier reportIdentifier : reportIdentifiers) {
				row.addCell(new TableCellHeadWidget(reportIdentifier.getId()));
			}
			table.addRow(row);
		}

		int counter = 0;
		while (reportValueIterator.hasNext() && counter < LIMIT) {
			counter++;
			final List<AnalyticsReportValue> reportValues = reportValueIterator.next();
			final TableRowWidget row = new TableRowWidget();
			{
				row.addCell(calendarUtil.toDateTimeString(reportValues.get(0).getDate()));
			}
			for (final AnalyticsReportValue reportValue : reportValues) {
				final TableCellWidget cell = new TableCellWidget(df.format(reportValue.getValue()));
				cell.addAttribute("sorttable_customkey", df.format(reportValue.getValue()));
				row.addCell(cell);
			}
			table.addRow(row);
		}
		return table;
	}

	@Override
	public List<JavascriptResource> getJavascriptResource(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	public AnalyticsReportChartType getType() {
		return AnalyticsReportChartType.TABLE;
	}

}
