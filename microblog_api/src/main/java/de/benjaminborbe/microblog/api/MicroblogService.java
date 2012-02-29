package de.benjaminborbe.microblog.api;

public interface MicroblogService {

	MicroblogPostIdentifier getLastRevision() throws MicroblogServiceException;

	void mailPost(MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void mailConversation(MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException;

	MicroblogConversationIdentifier createMicroblogConversationIdentifier(long conversationNumber);

	MicroblogPostIdentifier createMicroblogPostIdentifier(long postNumber);
}
