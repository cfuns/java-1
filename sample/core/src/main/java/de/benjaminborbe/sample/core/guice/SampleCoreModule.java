package de.benjaminborbe.sample.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.sample.api.SampleService;
import de.benjaminborbe.sample.core.service.SampleCoreServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SampleCoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SampleService.class).to(SampleCoreServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
