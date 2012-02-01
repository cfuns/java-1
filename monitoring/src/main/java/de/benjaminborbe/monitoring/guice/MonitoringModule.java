package de.benjaminborbe.monitoring.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.NodeCheckerCache;
import de.benjaminborbe.monitoring.service.MonitoringServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MonitoringService.class).to(MonitoringServiceImpl.class).in(Singleton.class);
		bind(NodeChecker.class).to(NodeCheckerCache.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
