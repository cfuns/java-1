package de.benjaminborbe.microblog.connector;

import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;

@Singleton
public class MicroblogRevisionStorageMock implements MicroblogRevisionStorage {

	private MicroblogPostIdentifier revision;

	@Override
	public MicroblogPostIdentifier getLastRevision() {
		return revision;
	}

	@Override
	public void setLastRevision(final MicroblogPostIdentifier revision) {
		this.revision = revision;
	}

}
