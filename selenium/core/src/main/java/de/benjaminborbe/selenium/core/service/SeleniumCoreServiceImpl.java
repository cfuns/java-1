package de.benjaminborbe.selenium.core.service;

import de.benjaminborbe.selenium.api.SeleniumService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SeleniumCoreServiceImpl implements SeleniumService {

	private final Logger logger;

	@Inject
	public SeleniumCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
	}

}
