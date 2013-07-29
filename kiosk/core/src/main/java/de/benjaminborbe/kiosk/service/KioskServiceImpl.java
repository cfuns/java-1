package de.benjaminborbe.kiosk.service;

import de.benjaminborbe.kiosk.KioskConstants;
import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;
import de.benjaminborbe.kiosk.api.KioskUser;
import de.benjaminborbe.kiosk.database.KioskDatabaseConnector;
import de.benjaminborbe.kiosk.database.KioskDatabaseConnectorException;
import de.benjaminborbe.kiosk.database.KioskUserBean;
import de.benjaminborbe.kiosk.util.KioskBookingMessage;
import de.benjaminborbe.kiosk.util.KioskBookingMessageMapper;
import de.benjaminborbe.message.api.MessageService;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.mapper.MapException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class KioskServiceImpl implements KioskService {

	private static final long DELAY = 5 * 60 * 1000; // 5 min

	private final Logger logger;

	private final KioskDatabaseConnector kioskDatabaseConnector;

	private final KioskBookingMessageMapper kioskBookingMessageMapper;

	private final MessageService messageService;

	private final CalendarUtil calendarUtil;

	private final CurrentTime currentTime;

	@Inject
	public KioskServiceImpl(
		final Logger logger,
		final CurrentTime currentTime,
		final CalendarUtil calendarUtil,
		final MessageService messageService,
		final KioskDatabaseConnector kioskDatabaseConnector,
		final KioskBookingMessageMapper kioskBookingMessageMapper
	) {
		this.logger = logger;
		this.currentTime = currentTime;
		this.calendarUtil = calendarUtil;
		this.messageService = messageService;
		this.kioskDatabaseConnector = kioskDatabaseConnector;
		this.kioskBookingMessageMapper = kioskBookingMessageMapper;
	}

	@Override
	public void book(final long customer, final long ean) throws KioskServiceException {
		try {
			logger.debug("book - customer: " + customer + " ean: " + ean);
			final KioskBookingMessage bookingMessage = new KioskBookingMessage(customer, ean);
			final String id = customer + "_" + ean;
			final Calendar startTime = calendarUtil.getCalendar(currentTime.currentTimeMillis() + DELAY);
			messageService.sendMessage(KioskConstants.BOOKING_MESSAGE_TYPE, id, kioskBookingMessageMapper.map(bookingMessage), startTime);
		} catch (final MessageServiceException e) {
			throw new KioskServiceException(e);
		} catch (MapException e) {
			throw new KioskServiceException(e);
		}
	}

	@Override
	public KioskUser getCustomerNumber(final String prename, final String surename) throws KioskServiceException {
		try {
			logger.debug("getCustomerNumber - prename: " + prename + " surename: " + surename);
			return kioskDatabaseConnector.getCustomerNumber(prename, surename);
		} catch (final KioskDatabaseConnectorException e) {
			throw new KioskServiceException(e);
		}
	}

	@Override
	public Collection<KioskUser> getBookingsForDay(final Calendar calendar, final long ean) throws KioskServiceException {
		try {
			logger.debug("getBookingsForDay - calendar: " + calendarUtil.toDateString(calendar) + " ean: " + ean);
			final List<KioskUser> result = new ArrayList<KioskUser>();
			for (final KioskUserBean user : kioskDatabaseConnector.getBookingsForDay(calendar, ean)) {
				result.add(user);
			}
			return result;
		} catch (final KioskDatabaseConnectorException e) {
			throw new KioskServiceException(e);
		}
	}

}
