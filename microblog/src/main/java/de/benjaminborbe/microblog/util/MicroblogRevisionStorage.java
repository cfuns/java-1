package de.benjaminborbe.microblog.util;

public interface MicroblogRevisionStorage {

	long getLastRevision() throws MicroblogRevisionStorageException;

	void setLastRevision(long revision) throws MicroblogRevisionStorageException;
}
