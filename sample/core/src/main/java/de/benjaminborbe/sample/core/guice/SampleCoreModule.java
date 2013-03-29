package de.benjaminborbe.sample.core.guice;

import de.benjaminborbe.sample.core.service.SampleCoreServiceImpl;
import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.sample.api.SampleService;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class SampleCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SampleService.class).to(SampleCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
