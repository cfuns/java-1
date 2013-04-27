package de.benjaminborbe.cache.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.cache.service.CacheServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class CacheModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CacheService.class).to(CacheServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
