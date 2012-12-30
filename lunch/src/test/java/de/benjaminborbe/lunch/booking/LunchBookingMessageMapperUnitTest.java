package de.benjaminborbe.lunch.booking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.TimeZone;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class LunchBookingMessageMapperUnitTest {

	@Test
	public void testMap() throws Exception {
		{
			final String customerNumber = null;
			final Calendar calendar = null;
			final LunchBookingMessage bookingMessage = new LunchBookingMessage(customerNumber, calendar);
			final LunchBookingMessageMapper mapper = getBookingMessageMapper();
			final String string = mapper.map(bookingMessage);
			assertNotNull(string);
			final LunchBookingMessage bookingMessageAfter = mapper.map(string);
			assertEquals(customerNumber, bookingMessageAfter.getCustomerNumber());
			assertEquals(calendar, bookingMessageAfter.getDate());
		}

		{
			final String customerNumber = "James Bond";
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			final LunchBookingMessage bookingMessage = new LunchBookingMessage(customerNumber, calendar);
			final LunchBookingMessageMapper mapper = getBookingMessageMapper();
			final String string = mapper.map(bookingMessage);
			assertNotNull(string);
			final LunchBookingMessage bookingMessageAfter = mapper.map(string);
			assertEquals(customerNumber, bookingMessageAfter.getCustomerNumber());
			assertEquals(calendar, bookingMessageAfter.getDate());
		}
	}

	private LunchBookingMessageMapper getBookingMessageMapper() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CurrentTimeImpl currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final Provider<LunchBookingMessage> p = new ProviderMock<LunchBookingMessage>(LunchBookingMessage.class);
		final MapperString mapperString = new MapperString();
		return new LunchBookingMessageMapperImpl(p, mapperCalendar, mapperString);
	}
}
