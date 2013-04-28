package de.benjaminborbe.index.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.configuration.tools.ConfigurationCache;
import de.benjaminborbe.configuration.tools.ConfigurationCacheImpl;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.index.config.IndexConfig;
import de.benjaminborbe.index.config.IndexConfigImpl;
import de.benjaminborbe.index.service.IndexServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class IndexModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationCache.class).to(ConfigurationCacheImpl.class);
		bind(IndexConfig.class).to(IndexConfigImpl.class).in(Singleton.class);
		bind(IndexService.class).to(IndexServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
