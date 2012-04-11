package de.benjaminborbe.messageservice.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.messageservice.api.MessageserviceService;
import de.benjaminborbe.messageservice.sample.SampleConfig;
import de.benjaminborbe.messageservice.sample.SampleConfigImpl;
import de.benjaminborbe.messageservice.service.MessageserviceServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MessageserviceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SampleConfig.class).to(SampleConfigImpl.class).in(Singleton.class);
		bind(MessageserviceService.class).to(MessageserviceServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
