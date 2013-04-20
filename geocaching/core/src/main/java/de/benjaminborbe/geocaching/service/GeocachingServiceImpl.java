package de.benjaminborbe.geocaching.service;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.geocaching.api.GeocachingService;

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
