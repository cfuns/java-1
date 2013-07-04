package de.benjaminborbe.util.gui.util;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class UtilGuiTimeConvertImplUnitTest {

	private TimeZone defaultTimeZone;

	@Before
	public void setUp() {
		defaultTimeZone = TimeZone.getDefault();
	}

	@After
	public void tearUp() {
		TimeZone.setDefault(defaultTimeZone);
	}

	@Test
	public void testConvert() throws Exception {

		final List<TimeZone> timeZones = Arrays.asList(TimeZone.getTimeZone("Europe/Berlin"), TimeZone.getTimeZone("UTC"));
		for (final TimeZone timeZone : timeZones) {
			TimeZone.setDefault(timeZone);

			final Logger logger = EasyMock.createNiceMock(Logger.class);
			EasyMock.replay(logger);

			final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
			final ParseUtil parseUtil = new ParseUtilImpl();
			final CurrentTime currentTime = new CurrentTimeImpl();
			final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

			final UtilGuiTimeConvert utilGuiTimeConvert = new UtilGuiTimeConvertImpl(calendarUtil);
			// winterzeit
			{
				final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				calendar.set(Calendar.YEAR, 2012);
				calendar.set(Calendar.MONTH, 12);
				calendar.set(Calendar.DAY_OF_MONTH, 24);
				calendar.set(Calendar.HOUR_OF_DAY, 10);
				calendar.set(Calendar.MINUTE, 15);
				calendar.set(Calendar.SECOND, 30);
				calendar.set(Calendar.MILLISECOND, 45);
				final Calendar result = utilGuiTimeConvert.convert(calendar, TimeZone.getTimeZone("Europe/Berlin"));
				assertEquals(11, result.get(Calendar.HOUR_OF_DAY));
			}

			// sommerzeit
			{
				final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				calendar.set(Calendar.YEAR, 2012);
				calendar.set(Calendar.MONTH, 6);
				calendar.set(Calendar.DAY_OF_MONTH, 24);
				calendar.set(Calendar.HOUR_OF_DAY, 10);
				calendar.set(Calendar.MINUTE, 15);
				calendar.set(Calendar.SECOND, 30);
				calendar.set(Calendar.MILLISECOND, 45);
				final Calendar result = utilGuiTimeConvert.convert(calendar, TimeZone.getTimeZone("Europe/Berlin"));
				assertEquals(12, result.get(Calendar.HOUR_OF_DAY));
			}
		}
	}
}
