package de.benjaminborbe.shortener.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.shortener.api.ShortenerService;

@Singleton
public class ShortenerServiceImpl implements ShortenerService {

	private final Logger logger;

	@Inject
	public ShortenerServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
