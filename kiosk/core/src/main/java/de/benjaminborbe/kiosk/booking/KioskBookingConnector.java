package de.benjaminborbe.kiosk.booking;

public interface KioskBookingConnector {

	boolean book(long customerNumber, long ean);

}
