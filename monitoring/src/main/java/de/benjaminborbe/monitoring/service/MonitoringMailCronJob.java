package de.benjaminborbe.monitoring.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.util.MonitoringMailer;

@Singleton
public class MonitoringMailCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final MonitoringMailer monitoringMailer;

	private final MonitoringConfig monitoringConfig;

	@Inject
	public MonitoringMailCronJob(final Logger logger, final MonitoringMailer monitoringMailer, final MonitoringConfig monitoringConfig) {
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
			logger.debug("monitoring cron => started");
			monitoringMailer.mail();
			logger.debug("monitoring cron => finished");
		}
		else {
			logger.debug("monitoring cron => skipped");
		}
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
