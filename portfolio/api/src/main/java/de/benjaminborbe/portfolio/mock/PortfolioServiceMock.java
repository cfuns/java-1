package de.benjaminborbe.portfolio.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.portfolio.api.PortfolioService;

@Singleton
public class PortfolioServiceMock implements PortfolioService {

	@Inject
	public PortfolioServiceMock() {
	}

	@Override
	public void execute() {
	}
}
