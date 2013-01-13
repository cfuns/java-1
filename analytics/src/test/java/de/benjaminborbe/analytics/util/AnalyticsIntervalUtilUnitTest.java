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

		final Calendar result = c.buildIntervalCalendar(calendar, AnalyticsReportInterval.DAY);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testDayNext() throws Exception {
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

		final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.DAY);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(23, result.get(Calendar.DAY_OF_MONTH));
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

		final Calendar result = c.buildIntervalCalendar(calendar, AnalyticsReportInterval.HOUR);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testHourNext() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();
		{
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

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.HOUR);
			assertEquals(2012, result.get(Calendar.YEAR));
			assertEquals(11, result.get(Calendar.MONTH));
			assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
			assertEquals(19, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
		{
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.set(2012, 11, 24, 0, 15, 45);
			calendar.set(Calendar.MILLISECOND, 59);

			assertEquals(2012, calendar.get(Calendar.YEAR));
			assertEquals(11, calendar.get(Calendar.MONTH));
			assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals(0, calendar.get(Calendar.HOUR_OF_DAY));
			assertEquals(15, calendar.get(Calendar.MINUTE));
			assertEquals(45, calendar.get(Calendar.SECOND));
			assertEquals(59, calendar.get(Calendar.MILLISECOND));

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.HOUR);
			assertEquals(2012, result.get(Calendar.YEAR));
			assertEquals(11, result.get(Calendar.MONTH));
			assertEquals(23, result.get(Calendar.DAY_OF_MONTH));
			assertEquals(23, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
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

		final Calendar result = c.buildIntervalCalendar(calendar, AnalyticsReportInterval.MINUTE);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testMinuteNext() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		{
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

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.MINUTE);
			assertEquals(2012, result.get(Calendar.YEAR));
			assertEquals(11, result.get(Calendar.MONTH));
			assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
			assertEquals(20, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(14, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
		{
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.set(2012, 11, 24, 20, 0, 45);
			calendar.set(Calendar.MILLISECOND, 59);

			assertEquals(2012, calendar.get(Calendar.YEAR));
			assertEquals(11, calendar.get(Calendar.MONTH));
			assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, calendar.get(Calendar.MINUTE));
			assertEquals(45, calendar.get(Calendar.SECOND));
			assertEquals(59, calendar.get(Calendar.MILLISECOND));

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.MINUTE);
			assertEquals(2012, result.get(Calendar.YEAR));
			assertEquals(11, result.get(Calendar.MONTH));
			assertEquals(24, result.get(Calendar.DAY_OF_MONTH));
			assertEquals(19, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(59, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
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

		final Calendar result = c.buildIntervalCalendar(calendar, AnalyticsReportInterval.MONTH);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(11, result.get(Calendar.MONTH));
		assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testMonthNext() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();

		{
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

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.MONTH);
			assertEquals(2012, result.get(Calendar.YEAR));
			assertEquals(10, result.get(Calendar.MONTH));
			assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
			assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}

		{
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.set(2012, 0, 24, 20, 15, 45);
			calendar.set(Calendar.MILLISECOND, 59);

			assertEquals(2012, calendar.get(Calendar.YEAR));
			assertEquals(0, calendar.get(Calendar.MONTH));
			assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
			assertEquals(15, calendar.get(Calendar.MINUTE));
			assertEquals(45, calendar.get(Calendar.SECOND));
			assertEquals(59, calendar.get(Calendar.MILLISECOND));

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.MONTH);
			assertEquals(2011, result.get(Calendar.YEAR));
			assertEquals(11, result.get(Calendar.MONTH));
			assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
			assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
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

		final Calendar result = c.buildIntervalCalendar(calendar, AnalyticsReportInterval.WEEK);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(week, result.get(Calendar.WEEK_OF_YEAR));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testWeekNext() throws Exception {
		final AnalyticsIntervalUtil c = new AnalyticsIntervalUtil();
		{
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

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.WEEK);
			assertEquals(2012, result.get(Calendar.YEAR));
			assertEquals(week - 1, result.get(Calendar.WEEK_OF_YEAR));
			assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
		{
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			calendar.set(2012, 0, 4, 20, 15, 45);
			calendar.set(Calendar.MILLISECOND, 59);

			assertEquals(2012, calendar.get(Calendar.YEAR));
			assertEquals(0, calendar.get(Calendar.MONTH));
			assertEquals(4, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
			assertEquals(15, calendar.get(Calendar.MINUTE));
			assertEquals(45, calendar.get(Calendar.SECOND));
			assertEquals(59, calendar.get(Calendar.MILLISECOND));

			final int week = calendar.get(Calendar.WEEK_OF_YEAR);

			final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.WEEK);
			assertEquals(2012 - 1, result.get(Calendar.YEAR));
			assertEquals(week + 52, result.get(Calendar.WEEK_OF_YEAR));
			assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
			assertEquals(0, result.get(Calendar.MINUTE));
			assertEquals(0, result.get(Calendar.SECOND));
			assertEquals(0, result.get(Calendar.MILLISECOND));
		}
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

		final Calendar result = c.buildIntervalCalendar(calendar, AnalyticsReportInterval.YEAR);
		assertEquals(2012, result.get(Calendar.YEAR));
		assertEquals(0, result.get(Calendar.MONTH));
		assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}

	@Test
	public void testYearNext() throws Exception {
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

		final Calendar result = c.buildIntervalCalendarNext(calendar, AnalyticsReportInterval.YEAR);
		assertEquals(2011, result.get(Calendar.YEAR));
		assertEquals(0, result.get(Calendar.MONTH));
		assertEquals(1, result.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, result.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, result.get(Calendar.MINUTE));
		assertEquals(0, result.get(Calendar.SECOND));
		assertEquals(0, result.get(Calendar.MILLISECOND));
	}
}
