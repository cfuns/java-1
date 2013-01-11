package de.benjaminborbe.analytics.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsService;

@Singleton
public class AnalyticsServiceImpl implements AnalyticsService {

	private final Logger logger;

	@Inject
	public AnalyticsServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
