package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailer;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailerException;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.microblog.post.MicroblogPostMailerException;
import de.benjaminborbe.tools.util.ParseException;

public class MicroblogPostMailerListener implements MicroblogPostListener {

	private final Logger logger;

	private final MicroblogPostMailer microblogPostMailer;

	private final MicroblogConversationFinder microblogConversationFinder;

	private final MicroblogConversationMailer microblogConversationMailer;

	@Inject
	public MicroblogPostMailerListener(
			final Logger logger,
			final MicroblogPostMailer microblogPostMailer,
			final MicroblogConversationFinder microblogConversationFinder,
			final MicroblogConversationMailer microblogConversationMailer) {
		this.logger = logger;
		this.microblogPostMailer = microblogPostMailer;
		this.microblogConversationFinder = microblogConversationFinder;
		this.microblogConversationMailer = microblogConversationMailer;
	}

	@Override
	public void onNewPost(final MicroblogPostIdentifier microblogPostIdentifier) {
		try {
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
		catch (final MicroblogPostMailerException e) {
			logger.trace(e.getClass().getName(), e);
		}
		catch (final ParseException e) {
			logger.trace(e.getClass().getName(), e);
		}
		catch (final MicroblogConversationMailerException e) {
			logger.trace(e.getClass().getName(), e);
		}
		catch (final MicroblogConnectorException e) {
			logger.trace(e.getClass().getName(), e);
		}
	}

}
