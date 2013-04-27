package de.benjaminborbe.monitoring.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.util.MonitoringNotifier;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MonitoringMailCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final MonitoringNotifier monitoringMailer;

	private final MonitoringConfig monitoringConfig;

	@Inject
	public MonitoringMailCronJob(final Logger logger, final MonitoringNotifier monitoringMailer, final MonitoringConfig monitoringConfig) {
		this.logger = logger;
		this.monitoringMailer = monitoringMailer;
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
			monitoringMailer.mail();
			logger.trace("monitoring cron => finished");
		} else {
			logger.trace("monitoring cron => skipped");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
