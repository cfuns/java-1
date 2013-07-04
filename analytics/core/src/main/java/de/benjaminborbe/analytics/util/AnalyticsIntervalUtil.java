package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportInterval;

import javax.inject.Inject;
import java.util.Calendar;

public class AnalyticsIntervalUtil {

	@Inject
	public AnalyticsIntervalUtil() {
	}

	public Calendar buildIntervalCalendarNext(final Calendar calendar, final AnalyticsReportInterval analyticsReportInterval) {
		final Calendar result = buildIntervalCalendar(calendar, analyticsReportInterval);
		if (AnalyticsReportInterval.MINUTE.equals(analyticsReportInterval)) {
			result.add(Calendar.MINUTE, -1);
		}
		if (AnalyticsReportInterval.HOUR.equals(analyticsReportInterval)) {
			result.add(Calendar.HOUR_OF_DAY, -1);
		}
		if (AnalyticsReportInterval.DAY.equals(analyticsReportInterval)) {
			result.add(Calendar.DAY_OF_MONTH, -1);
		}
		if (AnalyticsReportInterval.WEEK.equals(analyticsReportInterval)) {
			result.add(Calendar.WEEK_OF_YEAR, -1);
		}
		if (AnalyticsReportInterval.MONTH.equals(analyticsReportInterval)) {
			result.add(Calendar.MONTH, -1);
		}
		if (AnalyticsReportInterval.YEAR.equals(analyticsReportInterval)) {
			result.add(Calendar.YEAR, -1);
		}
		return result;
	}

	public Calendar buildIntervalCalendar(final Calendar calendar, final AnalyticsReportInterval analyticsReportInterval) {
		final Calendar result = Calendar.getInstance(calendar.getTimeZone());
		if (AnalyticsReportInterval.MINUTE.equals(analyticsReportInterval)) {
			result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			result.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			result.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			result.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
			result.set(Calendar.SECOND, 0);
			result.set(Calendar.MILLISECOND, 0);
		}
		if (AnalyticsReportInterval.HOUR.equals(analyticsReportInterval)) {
			result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			result.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			result.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
			result.set(Calendar.MINUTE, 0);
			result.set(Calendar.SECOND, 0);
			result.set(Calendar.MILLISECOND, 0);
		}
		if (AnalyticsReportInterval.DAY.equals(analyticsReportInterval)) {
			result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			result.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			result.set(Calendar.HOUR_OF_DAY, 0);
			result.set(Calendar.MINUTE, 0);
			result.set(Calendar.SECOND, 0);
			result.set(Calendar.MILLISECOND, 0);
		}
		if (AnalyticsReportInterval.WEEK.equals(analyticsReportInterval)) {
			result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			result.set(Calendar.MONTH, 0);
			result.set(Calendar.DAY_OF_MONTH, 1);
			result.set(Calendar.HOUR_OF_DAY, 0);
			result.set(Calendar.MINUTE, 0);
			result.set(Calendar.SECOND, 0);
			result.set(Calendar.MILLISECOND, 0);
			result.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR));
		}
		if (AnalyticsReportInterval.MONTH.equals(analyticsReportInterval)) {
			result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			result.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
			result.set(Calendar.DAY_OF_MONTH, 1);
			result.set(Calendar.HOUR_OF_DAY, 0);
			result.set(Calendar.MINUTE, 0);
			result.set(Calendar.SECOND, 0);
			result.set(Calendar.MILLISECOND, 0);
		}
		if (AnalyticsReportInterval.YEAR.equals(analyticsReportInterval)) {
			result.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
			result.set(Calendar.MONTH, 0);
			result.set(Calendar.DAY_OF_MONTH, 1);
			result.set(Calendar.HOUR_OF_DAY, 0);
			result.set(Calendar.MINUTE, 0);
			result.set(Calendar.SECOND, 0);
			result.set(Calendar.MILLISECOND, 0);
		}
		return result;
	}
}
