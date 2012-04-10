package de.benjaminborbe.streamcache.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.streamcache.api.StreamcacheService;

@Singleton
public class StreamcacheServiceImpl implements StreamcacheService {

	private final Logger logger;

	@Inject
	public StreamcacheServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
