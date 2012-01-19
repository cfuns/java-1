package de.benjaminborbe.performance.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.performance.util.PerformanceTracker;
import de.benjaminborbe.performance.util.PerformanceTrackerImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class PerformanceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PerformanceTracker.class).to(PerformanceTrackerImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
