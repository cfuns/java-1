package de.benjaminborbe.microblog.conversation;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;

public interface MicroblogConversationNotifier {

	void mailConversation(MicroblogConversationIdentifier conversationNumber) throws MicroblogConversationNotifierException;

}
