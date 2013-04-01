package de.benjaminborbe.monitoring.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.util.MonitoringChecker;

@Singleton
public class MonitoringCheckCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final MonitoringChecker monitoringChecker;

	private final MonitoringConfig monitoringConfig;

	@Inject
	public MonitoringCheckCronJob(final Logger logger, final MonitoringChecker monitoringChecker, final MonitoringConfig monitoringConfig) {
		this.logger = logger;
		this.monitoringChecker = monitoringChecker;
		this.monitoringConfig = monitoringConfig;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		if (monitoringConfig.isCronEnabled()) {
			logger.trace("monitoring cron => started");
			monitoringChecker.checkAll();
			logger.trace("monitoring cron => finished");
		}
		else {
			logger.trace("monitoring cron => skipped");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
