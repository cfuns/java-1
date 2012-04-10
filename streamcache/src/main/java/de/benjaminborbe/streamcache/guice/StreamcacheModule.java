package de.benjaminborbe.streamcache.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.streamcache.api.StreamcacheService;
import de.benjaminborbe.streamcache.service.StreamcacheServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class StreamcacheModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StreamcacheService.class).to(StreamcacheServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
