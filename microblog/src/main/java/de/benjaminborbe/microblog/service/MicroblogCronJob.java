package de.benjaminborbe.microblog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogConnectorException;
import de.benjaminborbe.microblog.util.MicroblogPostMailer;
import de.benjaminborbe.microblog.util.MicroblogPostMailerException;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorageException;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostMailer microblogPostMailer;

	@Inject
	public MicroblogCronJob(final Logger logger, final MicroblogConnector microblogConnector, final MicroblogRevisionStorage microblogRevisionStorage, final MicroblogPostMailer microblogPostMailer) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostMailer = microblogPostMailer;

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
			logger.debug("latestRevision in microblog: " + latestRevision);
			final Long lastestRevisionSend = microblogRevisionStorage.getLastRevision();
			if (lastestRevisionSend == null) {
				// no revision found in storage
				microblogRevisionStorage.setLastRevision(latestRevision);
			}
			else {
				logger.debug("latestRevision send: " + latestRevision);
				for (long rev = lastestRevisionSend + 1; rev <= latestRevision; ++rev) {
					microblogRevisionStorage.setLastRevision(rev);
					microblogPostMailer.mailPost(rev);
				}
			}
			logger.debug("done");
		}
		catch (final MicroblogConnectorException e) {
			logger.trace("MicroblogConnectorException", e);
		}
		catch (final MicroblogRevisionStorageException e) {
			logger.trace("MicroblogRevisionStorageException", e);
		}
		catch (final MicroblogPostMailerException e) {
			logger.trace("MicroblogRevisionStorageException", e);
		}

		logger.trace("MonitoringCronJob.execute() - finished");
	}

}
