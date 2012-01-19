package de.benjaminborbe.microblog.util;

public interface MicroblogRevisionStorage {

	long getLastRevision();

	void setLastRevision(long revision);
}
