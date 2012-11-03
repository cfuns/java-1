package de.benjaminborbe.tools.mapper;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class SingleMapCalendarUnitTest {

	public class T {

		private Calendar field;

		public Calendar getField() {
			return field;
		}

		public void setField(final Calendar field) {
			this.field = field;
		}

	}

	@Test
	public void testMap() {
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		final String name = "field";
		final long ms = 1351942796l * 1000l;

		final SingleMapCalendar<T> map = new SingleMapCalendar<T>(name, calendarUtil, parseUtil);
		final Calendar calendar = map.fromString(String.valueOf(ms));
		assertEquals(ms, calendar.getTimeInMillis());
		assertEquals(String.valueOf(ms), map.toString(calendar));
	}
}
