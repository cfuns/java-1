package de.benjaminborbe.wow.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.wow.api.WowService;
import de.benjaminborbe.wow.service.WowServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class WowModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(WowService.class).to(WowServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
