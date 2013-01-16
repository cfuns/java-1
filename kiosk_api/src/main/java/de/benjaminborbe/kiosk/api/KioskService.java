package de.benjaminborbe.kiosk.api;

public interface KioskService {

	void book(long customer, long ean) throws KioskServiceException;
}
