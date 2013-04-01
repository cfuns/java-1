package de.benjaminborbe.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.config.IndexConfig;
import de.benjaminborbe.index.config.IndexConfigImpl;
import de.benjaminborbe.index.service.IndexServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class IndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IndexConfig.class).to(IndexConfigImpl.class).in(Singleton.class);
		bind(IndexService.class).to(IndexServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
