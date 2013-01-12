package de.benjaminborbe.analytics.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import de.benjaminborbe.analytics.api.AnalyticsReportInterval;

public class AnalyticsIntervalUtilUnitTest {

	@Test
	public void testDay() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(2012, 11, 24, 20, 15, 45);
		calendar.set(Calendar.MILLISECOND, 59);

		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(45, calendar.get(Calendar.SECOND));
		assertEquals(59, calendar.get(Calendar.MILLISECOND));

		final Calendar result = c.buildCalendar(calendar, AnalyticsReportInterval.DAY);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testHour() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(2012, 11, 24, 20, 15, 45);
		calendar.set(Calendar.MILLISECOND, 59);

		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(45, calendar.get(Calendar.SECOND));
		assertEquals(59, calendar.get(Calendar.MILLISECOND));

		final Calendar result = c.buildCalendar(calendar, AnalyticsReportInterval.HOUR);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testMinute() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(2012, 11, 24, 20, 15, 45);
		calendar.set(Calendar.MILLISECOND, 59);

		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(45, calendar.get(Calendar.SECOND));
		assertEquals(59, calendar.get(Calendar.MILLISECOND));

		final Calendar result = c.buildCalendar(calendar, AnalyticsReportInterval.MINUTE);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testMonth() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(2012, 11, 24, 20, 15, 45);
		calendar.set(Calendar.MILLISECOND, 59);

		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(45, calendar.get(Calendar.SECOND));
		assertEquals(59, calendar.get(Calendar.MILLISECOND));

		final Calendar result = c.buildCalendar(calendar, AnalyticsReportInterval.MONTH);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testWeek() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(2012, 11, 24, 20, 15, 45);
		calendar.set(Calendar.MILLISECOND, 59);

		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(45, calendar.get(Calendar.SECOND));
		assertEquals(59, calendar.get(Calendar.MILLISECOND));

		final int week = calendar.get(Calendar.WEEK_OF_YEAR);

		final Calendar result = c.buildCalendar(calendar, AnalyticsReportInterval.WEEK);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(week, result.get(Calendar.WEEK_OF_YEAR));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testYear() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.set(2012, 11, 24, 20, 15, 45);
		calendar.set(Calendar.MILLISECOND, 59);

		assertEquals(2012, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(45, calendar.get(Calendar.SECOND));
		assertEquals(59, calendar.get(Calendar.MILLISECOND));

		final Calendar result = c.buildCalendar(calendar, AnalyticsReportInterval.YEAR);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(0, result.get(Calendar.MONTH));
		assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}
}
