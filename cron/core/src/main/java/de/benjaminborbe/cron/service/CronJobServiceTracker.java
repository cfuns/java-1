package de.benjaminborbe.cron.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.Quartz;
import de.benjaminborbe.tools.osgi.service.RegistryServiceTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;

import javax.inject.Singleton;

@Singleton
public class CronJobServiceTracker extends RegistryServiceTracker<CronJob> {

	private final Logger logger;

	private final Quartz quartz;

	public CronJobServiceTracker(final Logger logger, final Quartz quartz, final CronJobRegistry registry, final BundleContext context, final Class<?> clazz) {
		super(registry, context, clazz);
		this.logger = logger;
		this.quartz = quartz;
	}

	@Override
	protected void serviceRemoved(final CronJob cronJob) {
		logger.trace("CronActivator.serviceRemoved() - CronJob removed " + cronJob.getClass().getName());
		super.serviceRemoved(cronJob);
		quartz.removeCronJob(cronJob);
	}

	@Override
	protected void serviceAdded(final CronJob cronJob) {
		logger.trace("CronActivator.serviceAdded() - CronJob added " + cronJob.getClass().getName());
		super.serviceAdded(cronJob);
		quartz.addCronJob(cronJob);
	}

}
