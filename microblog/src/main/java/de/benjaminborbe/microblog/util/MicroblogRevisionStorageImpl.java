package de.benjaminborbe.microblog.util;

import com.google.inject.Singleton;

@Singleton
public class MicroblogRevisionStorageImpl implements MicroblogRevisionStorage {

	private long revision;

	@Override
	public long getLastRevision() {
		// TODO store in storageservice
		return revision;
	}

	@Override
	public void setLastRevision(final long revision) {
		// TODO read in storageservice
		this.revision = revision;
	}

}
