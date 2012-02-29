package de.benjaminborbe.microblog.conversation;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;

public interface MicroblogConversationMailer {

	void mailConversation(MicroblogConversationIdentifier conversationNumber) throws MicroblogConversationMailerException;

}
