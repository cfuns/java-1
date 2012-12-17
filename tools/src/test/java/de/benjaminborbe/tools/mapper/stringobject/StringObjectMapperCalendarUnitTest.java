package de.benjaminborbe.tools.mapper.stringobject;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperCalendar;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class StringObjectMapperCalendarUnitTest {

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

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final String name = "field";
		final long ms = 1351942796l * 1000l;

		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final StringObjectMapperCalendar<T> map = new StringObjectMapperCalendar<T>(name, mapperCalendar);
		final Calendar calendar = map.fromString(String.valueOf(ms));
		assertEquals(ms, calendar.getTimeInMillis());
		assertEquals(String.valueOf(ms), map.toString(calendar));
	}
}
