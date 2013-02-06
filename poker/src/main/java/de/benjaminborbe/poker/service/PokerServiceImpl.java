package de.benjaminborbe.poker.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.PokerService;

@Singleton
public class PokerServiceImpl implements PokerService {

	private final Logger logger;

	@Inject
	public PokerServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
