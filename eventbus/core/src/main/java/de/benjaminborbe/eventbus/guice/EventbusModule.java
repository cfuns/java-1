package de.benjaminborbe.eventbus.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.service.EventbusServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class EventbusModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventbusService.class).to(EventbusServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
