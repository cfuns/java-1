package de.benjaminborbe.cron.guice;

import com.google.inject.AbstractModule;
import javax.inject.Singleton;

import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.api.CronService;
import de.benjaminborbe.cron.message.CronMessageMapper;
import de.benjaminborbe.cron.message.CronMessageMapperImpl;
import de.benjaminborbe.cron.service.CronControllerImpl;
import de.benjaminborbe.cron.service.CronServiceImpl;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.CronJobRegistryImpl;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.cron.util.QuartzImpl;

public class CronModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CronMessageMapper.class).to(CronMessageMapperImpl.class).in(Singleton.class);
		bind(CronService.class).to(CronServiceImpl.class).in(Singleton.class);
		bind(CronController.class).to(CronControllerImpl.class).in(Singleton.class);
		bind(CronJobRegistry.class).to(CronJobRegistryImpl.class).in(Singleton.class);
		bind(Quartz.class).to(QuartzImpl.class).in(Singleton.class);
	}
}