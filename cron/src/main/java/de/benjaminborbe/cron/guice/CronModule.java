package de.benjaminborbe.cron.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.service.CronControllerImpl;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.CronJobRegistryImpl;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.cron.util.QuartzImpl;

public class CronModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CronController.class).to(CronControllerImpl.class).in(Singleton.class);
		bind(CronJobRegistry.class).to(CronJobRegistryImpl.class).in(Singleton.class);
		bind(Quartz.class).to(QuartzImpl.class).in(Singleton.class);
	}
}
