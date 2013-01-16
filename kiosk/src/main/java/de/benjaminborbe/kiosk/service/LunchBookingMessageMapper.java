package de.benjaminborbe.kiosk.service;

import de.benjaminborbe.tools.mapper.MapException;

public interface LunchBookingMessageMapper {

	String CUSTOMER = "customer";

	String EAN = "ean";

	String map(final LunchBookingMessage bookingMessage) throws MapException;

	LunchBookingMessage map(final String message) throws MapException;
}
