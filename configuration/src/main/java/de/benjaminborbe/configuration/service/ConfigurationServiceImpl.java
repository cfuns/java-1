package de.benjaminborbe.configuration.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.configuration.api.ConfigurationService;

@Singleton
public class ConfigurationServiceImpl implements ConfigurationService {

	private final Logger logger;

	@Inject
	public ConfigurationServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.debug("execute");
	}

}
