package de.benjaminborbe.monitoring.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;

@Singleton
public class MonitoringCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	@Inject
	public MonitoringCronJob(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("MonitoringCronJob.execute()");
		logger.trace("MonitoringCronJob.execute() - finished");
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
