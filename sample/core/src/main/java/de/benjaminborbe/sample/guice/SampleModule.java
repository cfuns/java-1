package de.benjaminborbe.sample.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.sample.api.SampleService;
import de.benjaminborbe.sample.service.SampleServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class SampleModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SampleService.class).to(SampleServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
