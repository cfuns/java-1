package de.benjaminborbe.wiki.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.service.WikiServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class WikiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WikiService.class).to(WikiServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
