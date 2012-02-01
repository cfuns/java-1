package de.benjaminborbe.microblog.util;

import de.benjaminborbe.microblog.api.MicroblogRevisionStorageException;

public interface MicroblogRevisionStorage {

	Long getLastRevision() throws MicroblogRevisionStorageException;

	void setLastRevision(long revision) throws MicroblogRevisionStorageException;
}
