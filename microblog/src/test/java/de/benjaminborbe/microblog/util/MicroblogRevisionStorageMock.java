package de.benjaminborbe.microblog.util;

import com.google.inject.Singleton;

@Singleton
public class MicroblogRevisionStorageMock implements MicroblogRevisionStorage {

	private long revision;

	@Override
	public long getLastRevision() {
		return revision;
	}

	@Override
	public void setLastRevision(final long revision) {
		this.revision = revision;
	}

}
