package de.benjaminborbe.lunch.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.lunch.api.LunchService;

@Singleton
public class LunchServiceImpl implements LunchService {

	private final Logger logger;

	@Inject
	public LunchServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
