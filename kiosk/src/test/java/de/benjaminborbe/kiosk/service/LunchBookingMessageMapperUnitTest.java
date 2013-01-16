package de.benjaminborbe.kiosk.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.kiosk.service.LunchBookingMessage;
import de.benjaminborbe.kiosk.service.LunchBookingMessageMapper;
import de.benjaminborbe.kiosk.service.LunchBookingMessageMapperImpl;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class LunchBookingMessageMapperUnitTest {

	@Test
	public void testMap() throws Exception {
		{
			final Long customer = null;
			final Long ean = null;
			final LunchBookingMessage bookingMessage = new LunchBookingMessage(customer, ean);
			final LunchBookingMessageMapper mapper = getBookingMessageMapper();
			final String string = mapper.map(bookingMessage);
			assertNotNull(string);
			final LunchBookingMessage bookingMessageAfter = mapper.map(string);
			assertEquals(customer, bookingMessageAfter.getCustomer());
			assertEquals(ean, bookingMessageAfter.getEan());
		}

		{
			final Long customer = 123l;
			final Long ean = 1337l;
			final LunchBookingMessage bookingMessage = new LunchBookingMessage(customer, ean);
			final LunchBookingMessageMapper mapper = getBookingMessageMapper();
			final String string = mapper.map(bookingMessage);
			assertNotNull(string);
			final LunchBookingMessage bookingMessageAfter = mapper.map(string);
			assertEquals(customer, bookingMessageAfter.getCustomer());
			assertEquals(ean, bookingMessageAfter.getEan());
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
		final MapperLong mapperLong = new MapperLong(parseUtil);
		return new LunchBookingMessageMapperImpl(p, mapperCalendar, mapperLong);
	}
}
