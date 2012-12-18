package de.benjaminborbe.lunch.kioskconnector;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class KioskConnectorImpl implements KioskConnector {

	private final Logger logger;

	@Inject
	public KioskConnectorImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean bookLunch(final String customerNumber) {
		logger.info("bookLunch: customerNumber");
		return false;
	}
}
