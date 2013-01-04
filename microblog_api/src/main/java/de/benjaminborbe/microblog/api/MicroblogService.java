package de.benjaminborbe.microblog.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MicroblogService {

	MicroblogPostIdentifier getLastRevision() throws MicroblogServiceException;

	void mailPost(MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void mailConversation(MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException;

	MicroblogConversationIdentifier createMicroblogConversationIdentifier(long conversationNumber);

	MicroblogPostIdentifier createMicroblogPostIdentifier(long postNumber);

	/**
	 * Return null is no conversation exists
	 */
	MicroblogConversationIdentifier getMicroblogConversationIdentifierForPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void refresh(SessionIdentifier sessionIdentifier) throws MicroblogServiceException, PermissionDeniedException, LoginRequiredException;
}
