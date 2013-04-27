package de.benjaminborbe.microblog.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;

public interface MicroblogService {

	MicroblogPostIdentifier getLatestPostIdentifier() throws MicroblogServiceException;

	void mailPost(MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void mailConversation(MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException;

	MicroblogConversationIdentifier createMicroblogConversationIdentifier(long conversationNumber);

	MicroblogPostIdentifier createMicroblogPostIdentifier(long postNumber);

	MicroblogConversationIdentifier getMicroblogConversationIdentifierForPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException;

	void refreshPost(SessionIdentifier sessionIdentifier) throws MicroblogServiceException, PermissionDeniedException, LoginRequiredException;

	void updatePost(
		SessionIdentifier sessionIdentifier,
		MicroblogPostIdentifier microblogPostIdentifier
	) throws MicroblogServiceException, PermissionDeniedException,
		LoginRequiredException;

	Collection<String> listNotifications(UserIdentifier userIdentifier) throws MicroblogServiceException;

	void activateNotification(UserIdentifier userIdentifier, String keyword) throws MicroblogServiceException, ValidationException;

	void deactivateNotification(UserIdentifier userIdentifier, String keyword) throws MicroblogServiceException, ValidationException;
}
