package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class MapperCalendarUnitTest {

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
		final long ms = 1351942796l * 1000l;

		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final Calendar calendar = mapperCalendar.fromString(String.valueOf(ms));
		assertEquals(ms, calendar.getTimeInMillis());
		assertEquals(String.valueOf(ms), mapperCalendar.toString(calendar));
	}
}
