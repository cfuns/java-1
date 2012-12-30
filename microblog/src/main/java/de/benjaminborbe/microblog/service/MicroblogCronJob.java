package de.benjaminborbe.microblog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.microblog.util.MicroblogPostListener;
import de.benjaminborbe.microblog.util.MicroblogPostListenerRegistry;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostListenerRegistry microblogPostListenerRegistry;

	@Inject
	public MicroblogCronJob(
			final Logger logger,
			final MicroblogConnector microblogConnector,
			final MicroblogRevisionStorage microblogRevisionStorage,
			final MicroblogPostListenerRegistry microblogPostListenerRegistry) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostListenerRegistry = microblogPostListenerRegistry;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("MonitoringCronJob.execute()");

		try {
			final MicroblogPostIdentifier latestRevision = microblogConnector.getLatestRevision();
			logger.trace("latestRevision in microblog: " + latestRevision);
			final MicroblogPostIdentifier lastestRevisionSend = microblogRevisionStorage.getLastRevision();
			if (lastestRevisionSend == null) {
				// no revision found in storage
				microblogRevisionStorage.setLastRevision(latestRevision);
			}
			else {
				logger.trace("latestRevision send: " + latestRevision);
				for (long rev = lastestRevisionSend.getId() + 1; rev <= latestRevision.getId(); ++rev) {
					final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(rev);
					microblogRevisionStorage.setLastRevision(microblogPostIdentifier);
					for (final MicroblogPostListener microblogPostListener : microblogPostListenerRegistry.getAll()) {
						microblogPostListener.onNewPost(microblogPostIdentifier);
					}
				}
			}
			logger.trace("done");
		}
		catch (final MicroblogConnectorException e) {
			logger.trace("MicroblogConnectorException", e);
		}
		catch (final MicroblogRevisionStorageException e) {
			logger.trace("MicroblogRevisionStorageException", e);
		}

		logger.trace("MonitoringCronJob.execute() - finished");
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
