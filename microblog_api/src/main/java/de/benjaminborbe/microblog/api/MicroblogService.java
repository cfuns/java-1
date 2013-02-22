package de.benjaminborbe.microblog.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MicroblogService {

	MicroblogPostIdentifier getLatestPostIdentifier() throws MicroblogServiceException;

	void mailPost(MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void mailConversation(MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException;

	MicroblogConversationIdentifier createMicroblogConversationIdentifier(long conversationNumber);

	MicroblogPostIdentifier createMicroblogPostIdentifier(long postNumber);

	MicroblogConversationIdentifier getMicroblogConversationIdentifierForPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void refreshPost(SessionIdentifier sessionIdentifier) throws MicroblogServiceException, PermissionDeniedException, LoginRequiredException;

	void updatePost(SessionIdentifier sessionIdentifier, MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException, PermissionDeniedException,
			LoginRequiredException;
}
