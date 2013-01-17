package de.benjaminborbe.kiosk.api;

import java.util.Calendar;
import java.util.Collection;

public interface KioskService {

	void book(long customer, long ean) throws KioskServiceException;

	KioskUser getCustomerNumber(String prename, String surename) throws KioskServiceException;

	Collection<KioskUser> getBookingsForDay(Calendar calendar, long ean) throws KioskServiceException;

}
