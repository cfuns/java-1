package de.benjaminborbe.tools.date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.TimeZone;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;

import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class CalendarUtilImplUnitTest {

	@Test
	public void testToDateString() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
		assertEquals("2011-12-24", u.toDateString(calendar));
	}

	@Test
	public void testToHourString() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
		assertEquals("20:15:13", u.toTimeString(calendar));
	}

	@Test
	public void testGetCalendar() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
		assertEquals(TimeZone.getTimeZone("UTF8"), calendar.getTimeZone());
		assertEquals(2011, calendar.get(Calendar.YEAR));
		assertEquals(11, calendar.get(Calendar.MONTH));
		assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
		assertEquals(15, calendar.get(Calendar.MINUTE));
		assertEquals(13, calendar.get(Calendar.SECOND));
	}

	@Test
	public void testClone() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
		final Calendar clone = u.clone(calendar);
		assertTrue(calendar.getTimeInMillis() == clone.getTimeInMillis());
		calendar.set(Calendar.YEAR, 2010);
		assertFalse(calendar.getTimeInMillis() == clone.getTimeInMillis());
	}

	@Test
	public void testAddDays() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
		final Calendar clone = u.addDays(calendar, 1);
		assertEquals("2011-12-25", u.toDateString(clone));
	}

	@Test
	public void testSubDays() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
		final Calendar clone = u.subDays(calendar, 1);
		assertEquals("2011-12-23", u.toDateString(clone));
	}

	@Test
	public void testGetWeekday() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		{
			final Calendar calendarMonday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 23, 20, 15, 13);
			assertEquals("monday", u.getWeekday(calendarMonday));
		}
		{
			final Calendar calendarTuesday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 24, 20, 15, 13);
			assertEquals("tuesday", u.getWeekday(calendarTuesday));
		}
		{
			final Calendar calendarWednesday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 25, 20, 15, 13);
			assertEquals("wednesday", u.getWeekday(calendarWednesday));
		}
		{
			final Calendar calendarThursday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 26, 20, 15, 13);
			assertEquals("thursday", u.getWeekday(calendarThursday));
		}
		{
			final Calendar calendarFriday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 27, 20, 15, 13);
			assertEquals("friday", u.getWeekday(calendarFriday));
		}
		{
			final Calendar calendarSaturday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 28, 20, 15, 13);
			assertEquals("saturday", u.getWeekday(calendarSaturday));
		}
		{
			final Calendar calendarSunday = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2012, 3, 29, 20, 15, 13);
			assertEquals("sunday", u.getWeekday(calendarSunday));
		}
	}

	@Test
	public void testDayEquals() {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			assertTrue(u.dayEquals(calendar1, calendar2));
		}
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 19, 15, 13);
			assertTrue(u.dayEquals(calendar1, calendar2));
		}
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 14, 13);
			assertTrue(u.dayEquals(calendar1, calendar2));
		}
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 12);
			assertTrue(u.dayEquals(calendar1, calendar2));
		}
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 23, 20, 15, 13);
			assertFalse(u.dayEquals(calendar1, calendar2));
		}
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 10, 24, 20, 15, 13);
			assertFalse(u.dayEquals(calendar1, calendar2));
		}
		{
			final Calendar calendar1 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			final Calendar calendar2 = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2010, 11, 24, 20, 15, 13);
			assertFalse(u.dayEquals(calendar1, calendar2));
		}
	}

	@Test
	public void testGetCalendarByTimestamp() {
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.expect(timeZoneUtil.getUTCTimeZone()).andReturn(TimeZone.getTimeZone("UTF8"));
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(null, timeZoneUtil);
		long time;
		{
			final Calendar calendar = u.getCalendar(TimeZone.getTimeZone("UTF8"), 2011, 11, 24, 20, 15, 13);
			assertNotNull(calendar);
			assertEquals(TimeZone.getTimeZone("UTF8"), calendar.getTimeZone());
			assertEquals(2011, calendar.get(Calendar.YEAR));
			assertEquals(11, calendar.get(Calendar.MONTH));
			assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
			assertEquals(15, calendar.get(Calendar.MINUTE));
			assertEquals(13, calendar.get(Calendar.SECOND));
			assertEquals(1324757713000l, calendar.getTimeInMillis());
			time = calendar.getTimeInMillis();
		}
		{
			final Calendar calendar = u.getCalendar(time);
			assertNotNull(calendar);
			assertEquals(TimeZone.getTimeZone("UTF8"), calendar.getTimeZone());
			assertEquals(2011, calendar.get(Calendar.YEAR));
			assertEquals(11, calendar.get(Calendar.MONTH));
			assertEquals(24, calendar.get(Calendar.DAY_OF_MONTH));
			assertEquals(20, calendar.get(Calendar.HOUR_OF_DAY));
			assertEquals(15, calendar.get(Calendar.MINUTE));
			assertEquals(13, calendar.get(Calendar.SECOND));
			assertEquals(1324757713000l, calendar.getTimeInMillis());
		}
	}

	@Ignore("TODO")
	@Test
	public void testGetCalendarSmart() throws Exception {
		final CalendarUtil u = new CalendarUtilImpl(null, null);
		assertEquals("2012-12-24", u.getCalendarSmart("1d"));
	}

	@Test
	public void testIsLE() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		assertTrue(u.isLE(u.parseDateTime(timeZone, "2012-12-24 20:15:50"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertTrue(u.isLE(u.parseDateTime(timeZone, "2012-12-24 20:15:51"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertFalse(u.isLE(u.parseDateTime(timeZone, "2012-12-24 20:15:52"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
	}

	@Test
	public void testIsGE() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		assertFalse(u.isGE(u.parseDateTime(timeZone, "2012-12-24 20:15:50"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertTrue(u.isGE(u.parseDateTime(timeZone, "2012-12-24 20:15:51"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertTrue(u.isGE(u.parseDateTime(timeZone, "2012-12-24 20:15:52"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
	}

	@Test
	public void testIsLT() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		assertTrue(u.isLT(u.parseDateTime(timeZone, "2012-12-24 20:15:50"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertFalse(u.isLT(u.parseDateTime(timeZone, "2012-12-24 20:15:51"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertFalse(u.isLT(u.parseDateTime(timeZone, "2012-12-24 20:15:52"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
	}

	@Test
	public void testIsGT() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		assertFalse(u.isGT(u.parseDateTime(timeZone, "2012-12-24 20:15:50"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertFalse(u.isGT(u.parseDateTime(timeZone, "2012-12-24 20:15:51"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertTrue(u.isGT(u.parseDateTime(timeZone, "2012-12-24 20:15:52"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
	}

	@Test
	public void testIsEQ() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		assertFalse(u.isEQ(u.parseDateTime(timeZone, "2012-12-24 20:15:50"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertTrue(u.isEQ(u.parseDateTime(timeZone, "2012-12-24 20:15:51"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
		assertFalse(u.isEQ(u.parseDateTime(timeZone, "2012-12-24 20:15:52"), u.parseDateTime(timeZone, "2012-12-24 20:15:51")));
	}

	@Test
	public void testParseDate() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		final Calendar c = u.parseDate(timeZone, "2012-11-09");
		assertEquals(2012, c.get(Calendar.YEAR));
		assertEquals(10, c.get(Calendar.MONTH));
		assertEquals(9, c.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, c.get(Calendar.MINUTE));
		assertEquals(0, c.get(Calendar.SECOND));
		assertEquals(0, c.get(Calendar.MILLISECOND));
	}

	@Test
	public void testParseDateToDate() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		final String dateString = "2012-11-09";
		final Calendar c = u.parseDate(timeZone, dateString);
		assertEquals(2012, c.get(Calendar.YEAR));
		assertEquals(10, c.get(Calendar.MONTH));
		assertEquals(9, c.get(Calendar.DAY_OF_MONTH));
		assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, c.get(Calendar.MINUTE));
		assertEquals(0, c.get(Calendar.SECOND));
		assertEquals(0, c.get(Calendar.MILLISECOND));
		assertEquals(dateString, u.toDateString(c));
	}

	@Test
	public void testParseDateToDatetime() throws Exception {
		final TimeZone timeZone = TimeZone.getDefault();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = EasyMock.createMock(TimeZoneUtil.class);
		EasyMock.replay(timeZoneUtil);
		final CalendarUtil u = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		final String dateString = "2012-11-09 20:45:59";
		final Calendar c = u.parseDateTime(timeZone, dateString);
		assertEquals(2012, c.get(Calendar.YEAR));
		assertEquals(10, c.get(Calendar.MONTH));
		assertEquals(9, c.get(Calendar.DAY_OF_MONTH));
		assertEquals(20, c.get(Calendar.HOUR_OF_DAY));
		assertEquals(45, c.get(Calendar.MINUTE));
		assertEquals(59, c.get(Calendar.SECOND));
		assertEquals(0, c.get(Calendar.MILLISECOND));
		assertEquals(dateString, u.toDateTimeString(c));
	}

}
