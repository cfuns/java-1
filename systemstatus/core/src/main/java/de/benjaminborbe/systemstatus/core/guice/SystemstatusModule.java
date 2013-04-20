package de.benjaminborbe.systemstatus.core.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.systemstatus.api.SystemstatusService;
import de.benjaminborbe.systemstatus.core.service.SystemstatusServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class SystemstatusModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SystemstatusService.class).to(SystemstatusServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
