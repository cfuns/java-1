package de.benjaminborbe.index.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.service.IndexSearcherServiceImpl;
import de.benjaminborbe.index.service.IndexerServiceImpl;
import de.benjaminborbe.index.util.IndexFactory;
import de.benjaminborbe.index.util.IndexFactoryImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class IndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IndexFactory.class).to(IndexFactoryImpl.class).in(Singleton.class);
		bind(IndexSearcherService.class).to(IndexSearcherServiceImpl.class).in(Singleton.class);
		bind(IndexerService.class).to(IndexerServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
