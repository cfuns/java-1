package de.benjaminborbe.dhl.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.dhl.util.DhlStatusChecker;

@Singleton
public class DhlStatusCheckCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final DhlStatusChecker dhlStatusChecker;

	@Inject
	public DhlStatusCheckCronJob(final Logger logger, final DhlStatusChecker dhlStatusChecker) {
		this.logger = logger;
		this.dhlStatusChecker = dhlStatusChecker;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("execute DhlCheckCronJob");
		dhlStatusChecker.check();

	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
