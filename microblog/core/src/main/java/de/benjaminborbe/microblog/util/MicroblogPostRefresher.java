package de.benjaminborbe.microblog.util;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MicroblogPostRefresher {

	private final class Action implements Runnable {

		@Override
		public void run() {

			try {
				final MicroblogPostIdentifier latestRevision = microblogConnector.getLatestRevision();
				logger.trace("latestRevision in microblog: " + latestRevision);
				final MicroblogPostIdentifier lastestRevisionSend = microblogRevisionStorage.getLastRevision();
				if (lastestRevisionSend == null) {
					// no revision found in storage
					microblogRevisionStorage.setLastRevision(latestRevision);
				} else {
					logger.trace("latestRevision send: " + latestRevision);
					for (long rev = lastestRevisionSend.getId() + 1; rev <= latestRevision.getId(); ++rev) {
						logger.trace("get post with revision: " + rev);
						final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(rev);
						microblogRevisionStorage.setLastRevision(microblogPostIdentifier);
						try {
							microblogPostUpdater.update(microblogPostIdentifier);
						} catch (MicroblogConnectorException e) {
							logger.debug(e.getClass().getName(), e);
						}
					}
				}
				logger.trace("done");
			} catch (final MicroblogRevisionStorageException | MicroblogConnectorException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
	}

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final MicroblogPostUpdater microblogPostUpdater;

	@Inject
	public MicroblogPostRefresher(
		final Logger logger,
		final MicroblogPostUpdater microblogPostUpdater,
		final MicroblogConnector microblogConnector,
		final MicroblogRevisionStorage microblogRevisionStorage,
		final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.microblogPostUpdater = microblogPostUpdater;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.runOnlyOnceATime = runOnlyOnceATime;
	}

	public boolean refresh() {
		logger.debug("microblog-refresh - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.debug("microblog-refresh - finished");
			return true;
		} else {
			logger.debug("microblog-refresh - skipped");
			return false;
		}
	}
}
