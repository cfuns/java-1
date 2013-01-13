package de.benjaminborbe.analytics.gui.chart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.benjaminborbe.analytics.api.AnalyticsReportValue;
import de.benjaminborbe.analytics.api.AnalyticsReportValueIterator;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.website.table.TableCellHeadWidget;
import de.benjaminborbe.website.table.TableCellWidget;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;

public class AnalyticsReportChartBuilderTable implements AnalyticsReportChartBuilder {

	private final CalendarUtil calendarUtil;

	@Inject
	public AnalyticsReportChartBuilderTable(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	@Override
	public Widget buildChart(final AnalyticsReportValueIterator reportValueIterator) throws AnalyticsServiceException {
		final DecimalFormat df = new DecimalFormat("#####0.0");

		final TableWidget table = new TableWidget();
		table.addClass("sortable");
		{
			final TableRowWidget row = new TableRowWidget();
			row.addCell(new TableCellHeadWidget("Name"));
			row.addCell(new TableCellHeadWidget("Value"));
			table.addRow(row);
		}
		while (reportValueIterator.hasNext()) {
			final AnalyticsReportValue reportValue = reportValueIterator.next();
			final TableRowWidget row = new TableRowWidget();
			{
				row.addCell(calendarUtil.toDateTimeString(reportValue.getDate()));
			}
			{
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
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}

	@Override
	public AnalyticsReportChartType getType() {
		return AnalyticsReportChartType.TABLE;
	}

}
