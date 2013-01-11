package de.benjaminborbe.analytics.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.service.AnalyticsServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;

public class AnalyticsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AnalyticsService.class).to(AnalyticsServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
