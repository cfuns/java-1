package de.benjaminborbe.microblog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogConnectorException;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorageException;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	@Inject
	public MicroblogCronJob(final Logger logger, final MicroblogConnector microblogConnector, final MicroblogRevisionStorage microblogRevisionStorage) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
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
			microblogRevisionStorage.setLastRevision(latestRevision);
			logger.debug("latestRevision = " + latestRevision);
		}
		catch (final MicroblogConnectorException e) {
			logger.debug("MicroblogConnectorException", e);
		}
		catch (final MicroblogRevisionStorageException e) {
			logger.debug("MicroblogRevisionStorageException", e);
		}

		// TODO

		// download rss feed

		// parse

		// send new

		logger.trace("MonitoringCronJob.execute() - finished");
	}
}
