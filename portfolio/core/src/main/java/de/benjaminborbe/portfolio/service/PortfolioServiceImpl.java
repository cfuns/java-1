package de.benjaminborbe.portfolio.service;

import de.benjaminborbe.portfolio.api.PortfolioService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PortfolioServiceImpl implements PortfolioService {

	private final Logger logger;

	@Inject
	public PortfolioServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void execute() {
		logger.trace("execute");
	}

}
