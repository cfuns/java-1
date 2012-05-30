package de.benjaminborbe.microblog.conversation;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.tools.util.ParseException;

public interface MicroblogConversationFinder {

	MicroblogConversationIdentifier findIdentifier(MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogConnectorException, ParseException;
}
