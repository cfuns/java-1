package de.benjaminborbe.kiosk.service;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.kiosk.KioskConstants;
import de.benjaminborbe.kiosk.connector.KioskBookingConnector;
import de.benjaminborbe.message.api.Message;
import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.mapper.MapException;

public class KioskBookingMessageConsumer implements MessageConsumer {

	private final Logger logger;

	private final LunchBookingMessageMapper bookingMessageMapper;

	private final KioskBookingConnector kioskConnector;

	@Inject
	public KioskBookingMessageConsumer(final Logger logger, final LunchBookingMessageMapper bookingMessageMapper, final KioskBookingConnector kioskConnector) {
		this.logger = logger;
		this.bookingMessageMapper = bookingMessageMapper;
		this.kioskConnector = kioskConnector;
	}

	@Override
	public String getType() {
		return KioskConstants.BOOKING_MESSAGE_TYPE;
	}

	@Override
	public boolean process(final Message message) {
		logger.debug("process");
		try {
			final LunchBookingMessage bookingMessage = bookingMessageMapper.map(message.getContent());
			logger.debug("book - customer: " + bookingMessage.getCustomer() + " ean: " + bookingMessage.getEan());

			return kioskConnector.book(bookingMessage.getCustomer(), bookingMessage.getEan());
		}
		catch (final MapException e) {
			logger.warn(e.getClass().getName(), e);
			return false;
		}
	}
}
