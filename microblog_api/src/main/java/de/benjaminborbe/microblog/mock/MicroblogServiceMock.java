package de.benjaminborbe.microblog.mock;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;

public class MicroblogServiceMock implements MicroblogService {

	@Override
	public MicroblogPostIdentifier getLastRevision() throws MicroblogServiceException {
		return null;
	}

	@Override
	public void mailPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException {
	}

	@Override
	public MicroblogConversationIdentifier createMicroblogConversationIdentifier(final long conversationNumber) {
		return null;
	}

	@Override
	public MicroblogPostIdentifier createMicroblogPostIdentifier(final long postNumber) {
		return null;
	}

	@Override
	public MicroblogConversationIdentifier getMicroblogConversationIdentifierForPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
		return null;
	}

	@Override
	public void refresh(final SessionIdentifier sessionIdentifier) throws MicroblogServiceException, PermissionDeniedException, LoginRequiredException {
	}

}
