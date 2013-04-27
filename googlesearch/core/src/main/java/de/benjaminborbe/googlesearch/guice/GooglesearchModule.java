package de.benjaminborbe.googlesearch.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.googlesearch.api.GooglesearchService;
import de.benjaminborbe.googlesearch.service.GooglesearchServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class GooglesearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GooglesearchService.class).to(GooglesearchServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
