package de.benjaminborbe.monitoring.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.check.Check;
import de.benjaminborbe.monitoring.check.CheckRegistry;

@Singleton
public class MonitoringCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final CheckRegistry checkRegistry;

	@Inject
	public MonitoringCronJob(final Logger logger, final CheckRegistry checkRegistry) {
		this.logger = logger;
		this.checkRegistry = checkRegistry;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.debug("MonitoringCronJob.execute()");
		for (final Check check : checkRegistry.getAll()) {
			try {
				if (check.check()) {
					logger.debug("[OK] " + check.getClass().getSimpleName());
				}
				else {
					logger.warn("[FAIL] " + check.getClass().getSimpleName());
				}
			}
			catch (final Exception e) {
				logger.warn("Check failed: " + check.getClass().getSimpleName(), e);
			}
		}
		logger.debug("MonitoringCronJob.execute() - finished");
	}
}
