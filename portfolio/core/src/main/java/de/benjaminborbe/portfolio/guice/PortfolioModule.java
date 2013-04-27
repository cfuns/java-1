package de.benjaminborbe.portfolio.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.portfolio.api.PortfolioService;
import de.benjaminborbe.portfolio.service.PortfolioServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class PortfolioModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PortfolioService.class).to(PortfolioServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
