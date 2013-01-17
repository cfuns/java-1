package de.benjaminborbe.microblog.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostListener;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailer;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailerException;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.microblog.post.MicroblogPostMailerException;
import de.benjaminborbe.tools.util.ParseException;

public class MicroblogPostListenerMailer implements MicroblogPostListener {

	private final Logger logger;

	private final MicroblogPostMailer microblogPostMailer;

	private final MicroblogConversationFinder microblogConversationFinder;

	private final MicroblogConversationMailer microblogConversationMailer;

	private final MicroblogConfig microblogConfig;

	@Inject
	public MicroblogPostListenerMailer(
			final Logger logger,
			final MicroblogConfig microblogConfig,
			final MicroblogPostMailer microblogPostMailer,
			final MicroblogConversationFinder microblogConversationFinder,
			final MicroblogConversationMailer microblogConversationMailer) {
		this.logger = logger;
		this.microblogConfig = microblogConfig;
		this.microblogPostMailer = microblogPostMailer;
		this.microblogConversationFinder = microblogConversationFinder;
		this.microblogConversationMailer = microblogConversationMailer;
	}

	@Override
	public void onNewPost(final MicroblogPost microblogPost) {
		try {
			if (microblogConfig.isMailEnabled()) {
				final MicroblogConversationIdentifier microblogConversationIdentifier = microblogConversationFinder.findIdentifier(microblogPost.getId());
				logger.trace("found microblogConversationIdentifier = " + microblogConversationIdentifier);
				if (microblogConversationIdentifier != null) {
					logger.trace("mailConversation: " + microblogConversationIdentifier);
					microblogConversationMailer.mailConversation(microblogConversationIdentifier);
				}
				else {
					logger.trace("mailPost: " + microblogPost.getId());
					microblogPostMailer.mailPost(microblogPost);
				}
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
