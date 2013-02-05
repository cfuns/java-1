package de.benjaminborbe.kiosk.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class KioskServiceImpl implements KioskService {

	private final Logger logger;

	private final KioskDatabaseConnector kioskDatabaseConnector;

	private final KioskBookingMessageMapper kioskBookingMessageMapper;

	private final MessageService messageService;

	private final CalendarUtil calendarUtil;

	@Inject
	public KioskServiceImpl(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final MessageService messageService,
			final KioskDatabaseConnector kioskDatabaseConnector,
			final KioskBookingMessageMapper kioskBookingMessageMapper) {
		this.logger = logger;
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
			messageService.sendMessage(KioskConstants.BOOKING_MESSAGE_TYPE, kioskBookingMessageMapper.map(bookingMessage));
		}
		catch (final MessageServiceException e) {
			throw new KioskServiceException(e);
		}
		catch (final MapException e) {
			throw new KioskServiceException(e);
		}
	}

	@Override
	public KioskUser getCustomerNumber(final String prename, final String surename) throws KioskServiceException {
		try {
			logger.debug("getCustomerNumber - prename: " + prename + " surename: " + surename);
			return kioskDatabaseConnector.getCustomerNumber(prename, surename);
		}
		catch (final KioskDatabaseConnectorException e) {
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
		}
		catch (final KioskDatabaseConnectorException e) {
			throw new KioskServiceException(e);
		}
	}

}
