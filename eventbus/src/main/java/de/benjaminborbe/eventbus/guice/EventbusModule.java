package de.benjaminborbe.eventbus.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.eventbus.api.aEventbusService;
import de.benjaminborbe.eventbus.service.EventbusServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class EventbusModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(aEventbusService.class).to(EventbusServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
