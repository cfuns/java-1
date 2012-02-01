package de.benjaminborbe.microblog.mock;

import de.benjaminborbe.microblog.api.MicroblogPostMailerException;
import de.benjaminborbe.microblog.api.MicroblogRevisionStorageException;
import de.benjaminborbe.microblog.api.MicroblogService;

public class MicroblogServiceMock implements MicroblogService {

	@Override
	public Long getLastRevision() throws MicroblogRevisionStorageException {
		return null;
	}

	@Override
	public void mailPost(final long number) throws MicroblogPostMailerException {
	}

}
