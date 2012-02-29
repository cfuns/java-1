package de.benjaminborbe.microblog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailer;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailerException;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.microblog.post.MicroblogPostMailerException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;

@Singleton
public class MicroblogServiceImpl implements MicroblogService {

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostMailer microblogPostMailer;

	private final MicroblogConversationMailer microblogConversationMailer;

	@Inject
	public MicroblogServiceImpl(
			final MicroblogRevisionStorage microblogRevisionStorage,
			final MicroblogPostMailer microblogPostMailer,
			final MicroblogConversationMailer microblogConversationMailer) {
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostMailer = microblogPostMailer;
		this.microblogConversationMailer = microblogConversationMailer;
	}

	@Override
	public MicroblogPostIdentifier getLastRevision() throws MicroblogServiceException {
		try {
			return microblogRevisionStorage.getLastRevision();
		}
		catch (final MicroblogRevisionStorageException e) {
			throw new MicroblogServiceException("MicroblogRevisionStorageException", e);
		}
	}

	@Override
	public void mailPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
		try {
			microblogPostMailer.mailPost(microblogPostIdentifier);
		}
		catch (final MicroblogPostMailerException e) {
			throw new MicroblogServiceException("MicroblogPostMailerException", e);
		}
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException {
		try {
			microblogConversationMailer.mailConversation(microblogConversationIdentifier);
		}
		catch (final MicroblogConversationMailerException e) {
			throw new MicroblogServiceException("MicroblogPostMailerException", e);
		}
	}

	@Override
	public MicroblogConversationIdentifier createMicroblogConversationIdentifier(final long conversationNumber) {
		return new MicroblogConversationIdentifier(conversationNumber);
	}

	@Override
	public MicroblogPostIdentifier createMicroblogPostIdentifier(final long postNumber) {
		return new MicroblogPostIdentifier(postNumber);
	}

}
