package de.benjaminborbe.googlesearch.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.googlesearch.api.GooglesearchService;
import de.benjaminborbe.googlesearch.service.GooglesearchServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class GooglesearchModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GooglesearchService.class).to(GooglesearchServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
