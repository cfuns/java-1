package de.benjaminborbe.sample.core.service;

import de.benjaminborbe.sample.api.SampleService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SampleCoreServiceImpl implements SampleService {

	private final Logger logger;

	@Inject
	public SampleCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

}
