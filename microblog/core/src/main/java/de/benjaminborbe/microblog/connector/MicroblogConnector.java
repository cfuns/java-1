package de.benjaminborbe.microblog.connector;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.conversation.MicroblogConversationResult;
import de.benjaminborbe.microblog.post.MicroblogPostResult;

public interface MicroblogConnector {

	MicroblogPostIdentifier getLatestRevision() throws MicroblogConnectorException;

	MicroblogPostResult getPost(final MicroblogPostIdentifier microblogConversationIdentifier) throws MicroblogConnectorException;

	MicroblogConversationResult getConversation(MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogConnectorException;
}
