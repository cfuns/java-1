package de.benjaminborbe.geocaching.service;

import de.benjaminborbe.geocaching.api.GeocachingService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeocachingServiceImpl implements GeocachingService {

	private final Logger logger;

	@Inject
	public GeocachingServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
