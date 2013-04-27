package de.benjaminborbe.cron.util;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;

public class GuiceJobFactory implements JobFactory {

	private final Injector injector;

	@Inject
	public GuiceJobFactory(final Injector inj) {
		injector = inj;
	}

	@Override
	public Job newJob(final TriggerFiredBundle triggerFiredBundle, final Scheduler scheduler) throws SchedulerException {
		Job job = null;
		try {
			// job = (Job) triggerFiredBundle.getJobDetail().getJobClass().newInstance();
			job = injector.getInstance(triggerFiredBundle.getJobDetail().getJobClass());
		} catch (final Exception ex) {
			throw new SchedulerException(ex);
		}

		injector.injectMembers(job);

		return job;
	}
}
