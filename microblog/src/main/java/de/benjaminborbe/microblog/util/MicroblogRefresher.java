package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;

public class MicroblogRefresher {

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
		}
	}

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostListenerRegistry microblogPostListenerRegistry;

	private final RunOnlyOnceATime runOnlyOnceATime;

	@Inject
	public MicroblogRefresher(
			final Logger logger,
			final MicroblogConnector microblogConnector,
			final MicroblogRevisionStorage microblogRevisionStorage,
			final MicroblogPostListenerRegistry microblogPostListenerRegistry,
			final RunOnlyOnceATime runOnlyOnceATime) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostListenerRegistry = microblogPostListenerRegistry;
		this.runOnlyOnceATime = runOnlyOnceATime;
	}

	public void refresh() {
		runOnlyOnceATime.run(new Action());
	}
}
