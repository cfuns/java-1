package de.benjaminborbe.performance.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;
import de.benjaminborbe.performance.api.PerformanceService;
import de.benjaminborbe.performance.util.PerformanceServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class PerformanceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PerformanceService.class).to(PerformanceServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
