package de.benjaminborbe.analytics.util;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class MapperCalendarFixLengthUnitTest {

	@Test
	public void testFixLength() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperCalendarFixLength mapperCalendarFixLength = new MapperCalendarFixLength(mapperCalendar, calendarUtil);

		{
			final Calendar calendarA = Calendar.getInstance();
			calendarA.setTimeInMillis(1l);
			final Calendar calendarB = Calendar.getInstance();
			calendarB.setTimeInMillis(Long.MAX_VALUE);
			assertEquals(mapperCalendarFixLength.toString(calendarA).length(), mapperCalendarFixLength.toString(calendarB).length());
		}

	}

	@Test
	public void testMapping() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperCalendarFixLength mapperCalendarFixLength = new MapperCalendarFixLength(mapperCalendar, calendarUtil);

		{
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(1l);
			assertEquals("0000000000000000001", mapperCalendarFixLength.toString(calendar));
		}
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.MAX_VALUE);
			assertEquals(String.valueOf(Long.MAX_VALUE), mapperCalendarFixLength.toString(calendar));
		}

		{
			assertEquals(1l, mapperCalendarFixLength.fromString(String.valueOf(1l)).getTimeInMillis());
		}
		{
			assertEquals(Long.MAX_VALUE, mapperCalendarFixLength.fromString(String.valueOf(Long.MAX_VALUE)).getTimeInMillis());
		}
	}
}
