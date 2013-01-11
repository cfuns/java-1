package de.benjaminborbe.projectile.gui.util;

import java.io.IOException;

import org.json.simple.JSONObject;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;

public class ProjectileReportToJsonConverter {

	@SuppressWarnings("unchecked")
	public JSONObject convert(final ProjectileSlacktimeReport report) throws IOException {
		final JSONObject object = new JSONObject();
		{
			object.put("username", report.getName());
		}
		{
			final JSONObject week = new JSONObject();
			week.put("intern", report.getWeekIntern());
			week.put("extern", report.getWeekExtern());
			object.put("week", week);
		}
		{
			final JSONObject month = new JSONObject();
			month.put("intern", report.getMonthIntern());
			month.put("extern", report.getMonthExtern());
			object.put("month", month);
		}
		{
			final JSONObject year = new JSONObject();
			year.put("intern", report.getYearIntern());
			year.put("extern", report.getYearExtern());
			object.put("year", year);
		}
		return object;
	}

}
