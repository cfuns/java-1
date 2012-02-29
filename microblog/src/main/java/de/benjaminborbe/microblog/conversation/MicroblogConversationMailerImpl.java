package de.benjaminborbe.microblog.conversation;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;

@Singleton
public class MicroblogConversationMailerImpl implements MicroblogConversationMailer {

	private final Logger logger;

	@Inject
	public MicroblogConversationMailerImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier conversationNumber) throws MicroblogConversationMailerException {
		logger.debug("mailConversation with rev " + conversationNumber);
	}

}
