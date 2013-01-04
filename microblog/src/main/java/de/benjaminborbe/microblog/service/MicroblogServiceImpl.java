package de.benjaminborbe.microblog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailer;
import de.benjaminborbe.microblog.conversation.MicroblogConversationMailerException;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.microblog.post.MicroblogPostMailerException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.microblog.util.MicroblogRefresher;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class MicroblogServiceImpl implements MicroblogService {

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostMailer microblogPostMailer;

	private final MicroblogConversationMailer microblogConversationMailer;

	private final MicroblogConversationFinder microblogConversationFinder;

	private final AuthorizationService authorizationService;

	private final MicroblogRefresher microblogRefresher;

	@Inject
	public MicroblogServiceImpl(
			final AuthorizationService authorizationService,
			final MicroblogRefresher microblogRefresher,
			final MicroblogRevisionStorage microblogRevisionStorage,
			final MicroblogPostMailer microblogPostMailer,
			final MicroblogConversationFinder microblogConversationFinder,
			final MicroblogConversationMailer microblogConversationMailer) {
		this.authorizationService = authorizationService;
		this.microblogRefresher = microblogRefresher;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostMailer = microblogPostMailer;
		this.microblogConversationFinder = microblogConversationFinder;
		this.microblogConversationMailer = microblogConversationMailer;
	}

	@Override
	public MicroblogPostIdentifier getLastRevision() throws MicroblogServiceException {
		try {
			return microblogRevisionStorage.getLastRevision();
		}
		catch (final MicroblogRevisionStorageException e) {
			throw new MicroblogServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void mailPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
		try {
			microblogPostMailer.mailPost(microblogPostIdentifier);
		}
		catch (final MicroblogPostMailerException e) {
			throw new MicroblogServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException {
		try {
			microblogConversationMailer.mailConversation(microblogConversationIdentifier);
		}
		catch (final MicroblogConversationMailerException e) {
			throw new MicroblogServiceException(e.getClass().getName(), e);
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

	@Override
	public MicroblogConversationIdentifier getMicroblogConversationIdentifierForPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
		try {
			return microblogConversationFinder.findIdentifier(microblogPostIdentifier);
		}
		catch (final MicroblogConnectorException e) {
			throw new MicroblogServiceException(e.getClass().getName(), e);
		}
		catch (final ParseException e) {
			throw new MicroblogServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void refresh(final SessionIdentifier sessionIdentifier) throws MicroblogServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			microblogRefresher.refresh();
		}
		catch (final AuthorizationServiceException e) {
			throw new MicroblogServiceException(e.getClass().getName(), e);
		}
	}

}
