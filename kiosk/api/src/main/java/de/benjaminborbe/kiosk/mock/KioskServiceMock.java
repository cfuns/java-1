package de.benjaminborbe.kiosk.mock;

import java.util.Calendar;
import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;
import de.benjaminborbe.kiosk.api.KioskUser;

@Singleton
public class KioskServiceMock implements KioskService {

	@Inject
	public KioskServiceMock() {
	}

	@Override
	public void book(final long customer, final long ean) throws KioskServiceException {
	}

	@Override
	public KioskUser getCustomerNumber(final String prename, final String surename) throws KioskServiceException {
		return null;
	}

	@Override
	public Collection<KioskUser> getBookingsForDay(final Calendar calendar, final long ean) throws KioskServiceException {
		return null;
	}

}
