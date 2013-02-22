package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

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
				}
				else {
					logger.debug("latestRevision send: " + latestRevision);
					for (long rev = lastestRevisionSend.getId() + 1; rev <= latestRevision.getId(); ++rev) {
						logger.debug("get post with revision: " + rev);
						final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(rev);
						microblogRevisionStorage.setLastRevision(microblogPostIdentifier);
						microblogPostUpdater.update(microblogPostIdentifier);
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
			final MicroblogPostListenerRegistry microblogPostListenerRegistry,
			final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.microblogPostUpdater = microblogPostUpdater;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.runOnlyOnceATime = runOnlyOnceATime;
	}

	public boolean refresh() {
		logger.trace("microblog-refresh - started");
		if (runOnlyOnceATime.run(new Action())) {
			logger.trace("microblog-refresh - finished");
			return true;
		}
		else {
			logger.trace("microblog-refresh - skipped");
			return false;
		}
	}
}
