package de.benjaminborbe.kiosk.connector;

public interface KioskBookingConnector {

	boolean book(long customerNumber, long ean);

}
