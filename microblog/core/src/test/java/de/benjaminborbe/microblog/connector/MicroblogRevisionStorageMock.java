package de.benjaminborbe.microblog.connector;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;

import javax.inject.Singleton;

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
