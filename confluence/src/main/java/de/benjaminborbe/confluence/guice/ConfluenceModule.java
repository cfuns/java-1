package de.benjaminborbe.confluence.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.service.ConfluenceServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class ConfluenceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfluenceService.class).to(ConfluenceServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
