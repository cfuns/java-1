package de.benjaminborbe.kiosk.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;

@Singleton
public class KioskServiceMock implements KioskService {

	@Inject
	public KioskServiceMock() {
	}

	@Override
	public void book(final long customer, final long ean) throws KioskServiceException {
	}

}
