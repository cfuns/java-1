package de.benjaminborbe.cron.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.cron.api.CronJob;

public class CronExecutor {

	private final CronJobRegistry cronJobRegistry;

	private final CronExecutionHistory cronExecutionHistory;

	private final Logger logger;

	@Inject
	public CronExecutor(final Logger logger, final CronJobRegistry cronJobRegistry, final CronExecutionHistory cronExecutionHistory) {
		this.logger = logger;
		this.cronJobRegistry = cronJobRegistry;
		this.cronExecutionHistory = cronExecutionHistory;
	}

	public void execute(final String name) {
		logger.trace("execute - starting job: " + name);
		final CronJob cronJob = cronJobRegistry.getByName(name);
		if (cronJob != null) {
			cronJob.execute();
			cronExecutionHistory.add(name);
			logger.trace("execute - finished job: " + name);
		}
		else {
			logger.error("execute - found no cronJob for name: " + name);
		}
	}
}
