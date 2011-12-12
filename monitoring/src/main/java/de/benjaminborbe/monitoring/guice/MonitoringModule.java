package de.benjaminborbe.monitoring.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.check.CheckLinker;
import de.benjaminborbe.monitoring.check.CheckRegistry;
import de.benjaminborbe.monitoring.check.CheckRegistryImpl;
import de.benjaminborbe.monitoring.check.SimpleCheck;
import de.benjaminborbe.monitoring.check.TcpCheckBuilder;
import de.benjaminborbe.monitoring.check.UrlCheckBuilder;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		// checks
		bind(SimpleCheck.class).in(Singleton.class);

		bind(CheckRegistry.class).to(CheckRegistryImpl.class).in(Singleton.class);
		requestStaticInjection(CheckLinker.class);
		requestStaticInjection(UrlCheckBuilder.class);
		requestStaticInjection(TcpCheckBuilder.class);
	}
}
