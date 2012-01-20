package de.benjaminborbe.microblog.util;

public interface MicroblogRevisionStorage {

	Long getLastRevision() throws MicroblogRevisionStorageException;

	void setLastRevision(long revision) throws MicroblogRevisionStorageException;
}
