package de.benjaminborbe.streamcache.core.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.streamcache.api.StreamcacheService;
import org.slf4j.Logger;

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
