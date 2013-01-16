package de.benjaminborbe.kiosk.service;

import de.benjaminborbe.tools.mapper.MapException;

public interface KioskBookingMessageMapper {

	String CUSTOMER = "customer";

	String EAN = "ean";

	String map(final KioskBookingMessage bookingMessage) throws MapException;

	KioskBookingMessage map(final String message) throws MapException;
}
