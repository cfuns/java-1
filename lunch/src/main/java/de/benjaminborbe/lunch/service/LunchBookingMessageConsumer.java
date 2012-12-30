package de.benjaminborbe.lunch.service;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.lunch.LunchConstants;
import de.benjaminborbe.lunch.booking.LunchBookingMessage;
import de.benjaminborbe.lunch.booking.LunchBookingMessageMapper;
import de.benjaminborbe.lunch.kioskconnector.KioskBookingConnector;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.MapException;

public class LunchBookingMessageConsumer implements MessageConsumer {

	private final Logger logger;

	private final LunchBookingMessageMapper bookingMessageMapper;

	private final CalendarUtil calendarUtil;

	private final KioskBookingConnector kioskConnector;

	@Inject
	public LunchBookingMessageConsumer(final Logger logger, final LunchBookingMessageMapper bookingMessageMapper, final CalendarUtil calendarUtil, final KioskBookingConnector kioskConnector) {
		this.logger = logger;
		this.bookingMessageMapper = bookingMessageMapper;
		this.calendarUtil = calendarUtil;
		this.kioskConnector = kioskConnector;
	}

	@Override
	public String getType() {
		return LunchConstants.BOOKING_MESSAGE_TYPE;
	}

	@Override
	public boolean process(final Message message) {
		logger.debug("process");
		try {
			final LunchBookingMessage bookingMessage = bookingMessageMapper.map(message.getContent());
			logger.debug("book - user: " + bookingMessage.getCustomerNumber() + " date: " + calendarUtil.toDateString(bookingMessage.getDate()));

			return kioskConnector.bookLunch(bookingMessage.getCustomerNumber());
		}
		catch (final MapException e) {
			logger.warn(e.getClass().getName(), e);
			return false;
		}
	}
}
