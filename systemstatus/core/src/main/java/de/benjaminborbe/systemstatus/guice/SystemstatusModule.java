package de.benjaminborbe.systemstatus.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.systemstatus.api.SystemstatusService;
import de.benjaminborbe.systemstatus.service.SystemstatusServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class SystemstatusModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SystemstatusService.class).to(SystemstatusServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
