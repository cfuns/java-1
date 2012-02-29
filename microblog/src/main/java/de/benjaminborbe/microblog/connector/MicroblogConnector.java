package de.benjaminborbe.microblog.connector;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.post.MicroblogPostResult;

public interface MicroblogConnector {

	public MicroblogPostIdentifier getLatestRevision() throws MicroblogConnectorException;

	public MicroblogPostResult getPost(final MicroblogPostIdentifier revision) throws MicroblogConnectorException;
}
