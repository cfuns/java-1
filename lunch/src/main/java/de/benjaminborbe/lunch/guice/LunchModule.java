package de.benjaminborbe.lunch.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.lunch.api.LunchService;
import de.benjaminborbe.lunch.config.LunchConfig;
import de.benjaminborbe.lunch.config.LunchConfigImpl;
import de.benjaminborbe.lunch.kioskconnector.KioskConnector;
import de.benjaminborbe.lunch.kioskconnector.KioskConnectorImpl;
import de.benjaminborbe.lunch.service.LunchServiceImpl;
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnector;
import de.benjaminborbe.lunch.wikiconnector.LunchWikiConnectorImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class LunchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(KioskConnector.class).to(KioskConnectorImpl.class).in(Singleton.class);
		bind(LunchWikiConnector.class).to(LunchWikiConnectorImpl.class).in(Singleton.class);
		bind(LunchConfig.class).to(LunchConfigImpl.class).in(Singleton.class);
		bind(LunchService.class).to(LunchServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
