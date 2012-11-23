package de.benjaminborbe.confluence.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;

@Singleton
public class ConfluenceRefreshCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?"; // ones per hour

	private final Logger logger;

	@Inject
	public ConfluenceRefreshCronJob(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.info("execute");
	}
}
