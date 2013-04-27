package de.benjaminborbe.performance.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.performance.api.PerformanceService;
import de.benjaminborbe.performance.util.PerformanceServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class PerformanceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PerformanceService.class).to(PerformanceServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
