package de.benjaminborbe.geocaching.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.geocaching.api.GeocachingService;
import de.benjaminborbe.geocaching.service.GeocachingServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class GeocachingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GeocachingService.class).to(GeocachingServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
