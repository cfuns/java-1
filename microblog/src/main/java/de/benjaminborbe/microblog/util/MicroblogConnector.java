package de.benjaminborbe.microblog.util;

import de.benjaminborbe.microblog.api.MicroblogConnectorException;

public interface MicroblogConnector {

	public long getLatestRevision() throws MicroblogConnectorException;

	public MicroblogPostResult getPost(final long revision) throws MicroblogConnectorException;
}
