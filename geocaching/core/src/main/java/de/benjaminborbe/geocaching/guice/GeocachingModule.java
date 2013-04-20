package de.benjaminborbe.geocaching.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.geocaching.api.GeocachingService;
import de.benjaminborbe.geocaching.service.GeocachingServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class GeocachingModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GeocachingService.class).to(GeocachingServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
