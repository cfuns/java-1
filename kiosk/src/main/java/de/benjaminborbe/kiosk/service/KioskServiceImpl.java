package de.benjaminborbe.kiosk.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.kiosk.api.KioskService;
import de.benjaminborbe.kiosk.api.KioskServiceException;

@Singleton
public class KioskServiceImpl implements KioskService {

	private final Logger logger;

	@Inject
	public KioskServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void book(final long customer, final long ean) throws KioskServiceException {
		logger.trace("book - customer: " + customer + " ean: " + ean);
	}

}
