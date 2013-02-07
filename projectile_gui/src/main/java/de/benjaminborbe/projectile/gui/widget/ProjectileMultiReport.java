package de.benjaminborbe.projectile.gui.widget;

import java.text.DecimalFormat;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.website.table.TableCellHeadWidget;
import de.benjaminborbe.website.table.TableCellWidget;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.CompositeWidget;

public class ProjectileMultiReport extends CompositeWidget {

	private final Collection<ProjectileSlacktimeReport> reportList;

	public ProjectileMultiReport(final Collection<ProjectileSlacktimeReport> reportList) {
		this.reportList = reportList;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {

		final TableWidget table = new TableWidget();
		table.addClass("sortable");
		{
			final TableRowWidget row = new TableRowWidget();
			row.addCell(new TableCellHeadWidget("Name"));
			row.addCell(new TableCellHeadWidget("Week %"));
			row.addCell(new TableCellHeadWidget("Week Extern"));
			row.addCell(new TableCellHeadWidget("Week Intern"));
			row.addCell(new TableCellHeadWidget("Month %"));
			row.addCell(new TableCellHeadWidget("Month Extern"));
			row.addCell(new TableCellHeadWidget("Month Intern"));
			row.addCell(new TableCellHeadWidget("Year %"));
			row.addCell(new TableCellHeadWidget("Year Extern"));
			row.addCell(new TableCellHeadWidget("Year Intern"));
			table.addRow(row);
		}
		for (final ProjectileSlacktimeReport report : reportList) {
			if (hasData(report)) {
				final TableRowWidget row = new TableRowWidget();
				row.addCell(report.getName());
				addCells(row, report.getWeekIntern(), report.getWeekExtern());
				addCells(row, report.getMonthIntern(), report.getMonthExtern());
				addCells(row, report.getYearIntern(), report.getYearExtern());
				table.addRow(row);
			}
		}
		return table;
	}

	private boolean hasData(final ProjectileSlacktimeReport report) {
		return report.getWeekIntern() != null && report.getWeekIntern() > 0 || report.getWeekExtern() != null && report.getWeekExtern() > 0 || report.getMonthIntern() != null
				&& report.getMonthIntern() > 0 || report.getMonthExtern() != null && report.getMonthExtern() > 0 || report.getYearIntern() != null && report.getYearIntern() > 0
				|| report.getYearExtern() != null && report.getYearExtern() > 0;
	}

	private void addCells(final TableRowWidget row, final Double intern, final Double extern) {
		if (extern != null && intern != null && (extern > 0 || intern > 0)) {
			final double total = extern + intern;
			final double procent = (extern / total);
			final DecimalFormat df = new DecimalFormat("#####0.0");
			final DecimalFormat dfHour = new DecimalFormat("#####0.0h");
			final DecimalFormat dfPercent = new DecimalFormat("#####0.0%");

			{
				final TableCellWidget cell = new TableCellWidget(dfPercent.format(procent));
				cell.addAttribute("sorttable_customkey", df.format(procent * 100));
				row.addCell(cell);
			}
			{
				final TableCellWidget cell = new TableCellWidget(dfHour.format(extern));
				cell.addAttribute("sorttable_customkey", df.format(extern));
				row.addCell(cell);
			}
			{
				final TableCellWidget cell = new TableCellWidget(dfHour.format(intern));
				cell.addAttribute("sorttable_customkey", df.format(intern));
				row.addCell(cell);
			}
		}
		else {
			row.addCell("-");
			row.addCell("-");
			row.addCell("-");
		}
	}

}
