package de.benjaminborbe.streamcache.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.streamcache.api.StreamcacheService;
import de.benjaminborbe.streamcache.core.service.StreamcacheServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class StreamcacheModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StreamcacheService.class).to(StreamcacheServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
