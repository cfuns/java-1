package de.benjaminborbe.monitoring.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.check.NodeChecker;
import de.benjaminborbe.monitoring.check.NodeCheckerImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class MonitoringModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
		bind(NodeChecker.class).to(NodeCheckerImpl.class).in(Singleton.class);
	}
}
