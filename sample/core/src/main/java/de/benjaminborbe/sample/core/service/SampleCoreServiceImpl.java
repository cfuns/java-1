package de.benjaminborbe.sample.core.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.sample.api.SampleService;

@Singleton
public class SampleCoreServiceImpl implements SampleService {

	private final Logger logger;

	@Inject
	public SampleCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(long value) {
		logger.trace("execute");
		return value * 2;
	}

}
