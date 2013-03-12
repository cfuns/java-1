package de.benjaminborbe.projectile.gui.util;

import java.io.IOException;
import java.util.Calendar;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;

public class ProjectileReportToJsonConverter {

	private final CalendarUtil calendarUtil;

	@Inject
	public ProjectileReportToJsonConverter(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	public JSONObject convert(final ProjectileSlacktimeReport report) throws IOException {
		final JSONObject object = new JSONObjectSimple();
		{
			object.put("username", report.getName());
		}
		{
			final JSONObject week = new JSONObjectSimple();
			week.put("intern", report.getWeekIntern());
			week.put("extern", report.getWeekExtern());
			week.put("updateDate", toDateTime(report.getWeekUpdateDate()));
			week.put("updateTime", toTimestamp(report.getWeekUpdateDate()));
			object.put("week", week);
		}
		{
			final JSONObject month = new JSONObjectSimple();
			month.put("intern", report.getMonthIntern());
			month.put("extern", report.getMonthExtern());
			month.put("updateDate", toDateTime(report.getMonthUpdateDate()));
			month.put("updateTime", toTimestamp(report.getMonthUpdateDate()));
			object.put("month", month);
		}
		{
			final JSONObject year = new JSONObjectSimple();
			year.put("intern", report.getYearIntern());
			year.put("extern", report.getYearExtern());
			year.put("updateDate", toDateTime(report.getYearUpdateDate()));
			year.put("updateTime", toTimestamp(report.getYearUpdateDate()));
			object.put("year", year);
		}
		return object;
	}

	private String toTimestamp(final Calendar updateDate) {
		if (updateDate == null) {
			return null;
		}
		return String.valueOf(updateDate.getTimeInMillis());
	}

	private String toDateTime(final Calendar updateDate) {
		if (updateDate == null) {
			return null;
		}
		return calendarUtil.toDateTimeString(updateDate);
	}
}
