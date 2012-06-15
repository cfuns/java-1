package de.benjaminborbe.microblog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailer;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailerException;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.microblog.post.MicroblogPostMailerException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostMailer microblogPostMailer;

	private final MicroblogConversationFinder microblogConversationFinder;

	private final MicroblogConversationMailer microblogConversationMailer;

	@Inject
	public MicroblogCronJob(
			final Logger logger,
			final MicroblogConnector microblogConnector,
			final MicroblogRevisionStorage microblogRevisionStorage,
			final MicroblogConversationFinder microblogConversationFinder,
			final MicroblogPostMailer microblogPostMailer,
			final MicroblogConversationMailer microblogConversationMailer) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogConversationFinder = microblogConversationFinder;
		this.microblogPostMailer = microblogPostMailer;
		this.microblogConversationMailer = microblogConversationMailer;
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
					microblogRevisionStorage.setLastRevision(new MicroblogPostIdentifier(rev));
					final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(rev);
					final MicroblogConversationIdentifier microblogConversationIdentifier = microblogConversationFinder.findIdentifier(microblogPostIdentifier);
					logger.trace("found microblogConversationIdentifier = " + microblogConversationIdentifier);
					if (microblogConversationIdentifier != null) {
						logger.trace("mailConversation: " + microblogConversationIdentifier);
						microblogConversationMailer.mailConversation(microblogConversationIdentifier);
					}
					else {
						logger.trace("mailPost: " + microblogPostIdentifier);
						microblogPostMailer.mailPost(microblogPostIdentifier);
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
		catch (final MicroblogPostMailerException e) {
			logger.trace("MicroblogRevisionStorageException", e);
		}
		catch (final ParseException e) {
			logger.trace("ParseException", e);
		}
		catch (final MicroblogConversationMailerException e) {
			logger.trace("MicroblogConversationMailerException", e);
		}

		logger.trace("MonitoringCronJob.execute() - finished");
	}

}
