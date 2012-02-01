package de.benjaminborbe.microblog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogPostMailerException;
import de.benjaminborbe.microblog.api.MicroblogRevisionStorageException;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.util.MicroblogPostMailer;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;

@Singleton
public class MicroblogServiceImpl implements MicroblogService {

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostMailer microblogPostMailer;

	@Inject
	public MicroblogServiceImpl(final MicroblogRevisionStorage microblogRevisionStorage, final MicroblogPostMailer microblogPostMailer) {
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostMailer = microblogPostMailer;
	}

	@Override
	public Long getLastRevision() throws MicroblogRevisionStorageException {
		return microblogRevisionStorage.getLastRevision();
	}

	@Override
	public void mailPost(final long number) throws MicroblogPostMailerException {
		microblogPostMailer.mailPost(number);
	}

}
