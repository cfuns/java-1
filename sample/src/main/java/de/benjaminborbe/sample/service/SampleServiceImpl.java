package de.benjaminborbe.sample.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.sample.api.SampleService;

@Singleton
public class SampleServiceImpl implements SampleService {

	private final Logger logger;

	@Inject
	public SampleServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.debug("execute");
	}

}
