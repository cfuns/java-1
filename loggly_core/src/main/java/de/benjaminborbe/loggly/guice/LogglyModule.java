package de.benjaminborbe.loggly.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.loggly.api.LogglyService;
import de.benjaminborbe.loggly.config.LogglyConfig;
import de.benjaminborbe.loggly.config.LogglyConfigImpl;
import de.benjaminborbe.loggly.service.LogglyServiceImpl;
import de.benjaminborbe.loggly.util.LogglyConnector;
import de.benjaminborbe.loggly.util.LogglyConnectorImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class LogglyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LogglyConfig.class).to(LogglyConfigImpl.class).in(Singleton.class);
		bind(LogglyConnector.class).to(LogglyConnectorImpl.class).in(Singleton.class);
		bind(LogglyService.class).to(LogglyServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
