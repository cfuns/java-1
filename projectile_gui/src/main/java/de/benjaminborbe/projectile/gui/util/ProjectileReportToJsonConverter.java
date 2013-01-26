package de.benjaminborbe.projectile.gui.util;

import java.io.IOException;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;

public class ProjectileReportToJsonConverter {

	public JSONObject convert(final ProjectileSlacktimeReport report) throws IOException {
		final JSONObject object = new JSONObjectSimple();
		{
			object.put("username", report.getName());
		}
		{
			final JSONObject week = new JSONObjectSimple();
			week.put("intern", report.getWeekIntern());
			week.put("extern", report.getWeekExtern());
			object.put("week", week);
		}
		{
			final JSONObject month = new JSONObjectSimple();
			month.put("intern", report.getMonthIntern());
			month.put("extern", report.getMonthExtern());
			object.put("month", month);
		}
		{
			final JSONObject year = new JSONObjectSimple();
			year.put("intern", report.getYearIntern());
			year.put("extern", report.getYearExtern());
			object.put("year", year);
		}
		return object;
	}

}
