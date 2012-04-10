package de.benjaminborbe.cron.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.util.CronExecutionHistory;
import de.benjaminborbe.cron.util.CronJobRegistry;

@Singleton
@DisallowConcurrentExecution
public class CronJobOsgi implements Job {

	private final Logger logger;

	private final CronJobRegistry cronJobRegistry;

	private final CronExecutionHistory cronExecutionHistory;

	@Inject
	public CronJobOsgi(final Logger logger, final CronJobRegistry cronJobRegistry, final CronExecutionHistory cronExecutionHistory) {
		this.logger = logger;
		this.cronJobRegistry = cronJobRegistry;
		this.cronExecutionHistory = cronExecutionHistory;
	}

	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		final String name = (String) context.getJobDetail().getJobDataMap().get("name");
		try {
			logger.trace("CronJobOsgi.execute - starting job: " + name);
			final CronJob cronJob = cronJobRegistry.getByName(name);
			if (cronJob != null) {
				cronJob.execute();
				cronExecutionHistory.add(name);
				logger.trace("CronJobOsgi.execute - finished job: " + name);
			}
			else {
				logger.error("CronJobOsgi.execute - found no cronJob for name: " + name);
			}
		}
		catch (final Exception e) {
			logger.trace("CronJobOsgi.execute - failed job: " + name + " exception: " + e.getClass().getSimpleName(), e);
		}
	}
}
