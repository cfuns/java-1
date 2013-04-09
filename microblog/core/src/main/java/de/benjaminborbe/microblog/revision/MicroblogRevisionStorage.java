package de.benjaminborbe.microblog.revision;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;

public interface MicroblogRevisionStorage {

	MicroblogPostIdentifier getLastRevision() throws MicroblogRevisionStorageException;

	void setLastRevision(MicroblogPostIdentifier revision) throws MicroblogRevisionStorageException;
}
