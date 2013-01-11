package de.benjaminborbe.projectile.gui.widget;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;

public class ProjectileSingleReport extends CompositeWidget {

	private final ProjectileSlacktimeReport report;

	public ProjectileSingleReport(final ProjectileSlacktimeReport report) {
		this.report = report;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		if (report != null) {
			widgets.add(createBlock("Week", report.getWeekIntern(), report.getWeekExtern(), report.getWeekBillable(), report.getWeekTarget()));
			widgets.add(createBlock("Month", report.getMonthIntern(), report.getMonthExtern(), report.getMonthBillable(), report.getMonthTarget()));
			widgets.add(createBlock("Year", report.getYearIntern(), report.getYearExtern(), report.getYearBillable(), report.getYearTarget()));
		}
		else {
			widgets.add("no data found");
		}
		return widgets;
	}

	private Widget createBlock(final String name, final Double intern, final Double extern, final Double billable, final Double target) {
		final ListWidget widgets = new ListWidget();
		widgets.add(new H2Widget(name));
		if (extern != null && intern != null && (extern + intern > 0)) {
			final double total = extern + intern;
			final double externPercent = (extern / total) * 100;
			final Double billableExternPercent = billable != null && extern > 0 ? ((billable / extern) * 100) : null;
			final DecimalFormat df = new DecimalFormat("#####0.0h");
			widgets.add("Extern: ");
			widgets.add(df.format(externPercent) + "%");
			widgets.add(" ");
			widgets.add("Billable: ");
			widgets.add(billableExternPercent != null ? (df.format(billableExternPercent) + "%") : "~");
			widgets.add(" ");
			widgets.add("(");
			widgets.add("total: " + df.format(total));
			widgets.add(" ");
			widgets.add("extern: " + df.format(extern));
			widgets.add(" ");
			widgets.add("intern: " + df.format(intern));
			widgets.add(" ");
			widgets.add("billable: ");
			widgets.add(billable != null ? df.format(billable) : "~");
			widgets.add(" ");
			widgets.add("target: ");
			widgets.add(target != null ? df.format(target) : "~");
			widgets.add(")");
		}
		else {
			widgets.add("-");
		}
		return widgets;
	}

}
