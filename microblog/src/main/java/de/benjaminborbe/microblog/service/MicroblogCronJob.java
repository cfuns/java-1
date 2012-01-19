package de.benjaminborbe.microblog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogConnectorException;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 */5 * * * ?";

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	@Inject
	public MicroblogCronJob(final Logger logger, final MicroblogConnector microblogConnector) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("MonitoringCronJob.execute()");

		try {
			final long latestRevision = microblogConnector.getLatestRevision();
			logger.debug("latestRevision = " + latestRevision);
		}
		catch (final MicroblogConnectorException e) {
			logger.debug("MicroblogConnectorException", e);
		}

		// TODO

		// download rss feed

		// parse

		// send new

		logger.trace("MonitoringCronJob.execute() - finished");
	}
}
