package de.benjaminborbe.cron.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.CronJobRegistryImpl;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.cron.util.QuartzImpl;

public class CronModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CronJobRegistry.class).to(CronJobRegistryImpl.class).in(Singleton.class);
		bind(Quartz.class).to(QuartzImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
