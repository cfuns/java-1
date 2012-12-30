package de.benjaminborbe.monitoring.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.monitoring.util.MonitoringMailer;

@Singleton
public class MonitoringCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final MonitoringMailer monitoringMailer;

	@Inject
	public MonitoringCronJob(final Logger logger, final MonitoringMailer monitoringMailer) {
		this.logger = logger;
		this.monitoringMailer = monitoringMailer;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("MonitoringCronJob.execute()");
		monitoringMailer.run();
		logger.trace("MonitoringCronJob.execute() - finished");
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
