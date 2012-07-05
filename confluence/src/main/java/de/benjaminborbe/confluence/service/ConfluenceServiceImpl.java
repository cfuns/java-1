package de.benjaminborbe.confluence.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceService;

@Singleton
public class ConfluenceServiceImpl implements ConfluenceService {

	private final Logger logger;

	@Inject
	public ConfluenceServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
