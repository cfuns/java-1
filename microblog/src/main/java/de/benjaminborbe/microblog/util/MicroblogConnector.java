package de.benjaminborbe.microblog.util;

public interface MicroblogConnector {

	public long getLatestRevision() throws MicroblogConnectorException;

	public MicroblogPostResult getPost(final long revision) throws MicroblogConnectorException;
}
