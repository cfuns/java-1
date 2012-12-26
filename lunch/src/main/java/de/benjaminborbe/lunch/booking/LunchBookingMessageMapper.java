package de.benjaminborbe.lunch.booking;

import de.benjaminborbe.tools.mapper.MapException;

public interface LunchBookingMessageMapper {

	String DATE = "date";

	String CUSTOMER_NUMBER = "customerNumber";

	String map(final LunchBookingMessage bookingMessage) throws MapException;

	LunchBookingMessage map(final String message) throws MapException;
}
