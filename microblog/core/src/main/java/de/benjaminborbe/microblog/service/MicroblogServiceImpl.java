package de.benjaminborbe.microblog.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.microblog.api.MicroblogServiceException;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.conversation.MicroblogConversationNotifier;
import de.benjaminborbe.microblog.conversation.MicroblogConversationNotifierException;
import de.benjaminborbe.microblog.post.MicroblogPostNotifier;
import de.benjaminborbe.microblog.post.MicroblogPostNotifierException;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorageException;
import de.benjaminborbe.microblog.util.MicroblogNotification;
import de.benjaminborbe.microblog.util.MicroblogPostRefresher;
import de.benjaminborbe.microblog.util.MicroblogPostUpdater;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseException;
import org.slf4j.Logger;

import java.util.Collection;

@Singleton
public class MicroblogServiceImpl implements MicroblogService {

	private static final int DURATION_WARN = 300;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MicroblogPostNotifier microblogPostMailer;

	private final MicroblogConversationNotifier microblogConversationMailer;

	private final MicroblogConversationFinder microblogConversationFinder;

	private final AuthorizationService authorizationService;

	private final MicroblogPostRefresher microblogPostRefresher;

	private final MicroblogPostUpdater microblogPostUpdater;

	private final Logger logger;

	private final DurationUtil durationUtil;

	private final MicroblogNotification microblogNotification;

	@Inject
	public MicroblogServiceImpl(
		final Logger logger,
		final DurationUtil durationUtil,
		final AuthorizationService authorizationService,
		final MicroblogConnector microblogConnector,
		final MicroblogPostUpdater microblogPostUpdater,
		final MicroblogPostRefresher microblogRefresher,
		final MicroblogRevisionStorage microblogRevisionStorage,
		final MicroblogPostNotifier microblogPostMailer,
		final MicroblogConversationFinder microblogConversationFinder,
		final MicroblogConversationNotifier microblogConversationMailer,
		final MicroblogNotification microblogNotification) {
		this.logger = logger;
		this.durationUtil = durationUtil;
		this.microblogConnector = microblogConnector;
		this.microblogPostUpdater = microblogPostUpdater;
		this.authorizationService = authorizationService;
		this.microblogPostRefresher = microblogRefresher;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.microblogPostMailer = microblogPostMailer;
		this.microblogConversationFinder = microblogConversationFinder;
		this.microblogConversationMailer = microblogConversationMailer;
		this.microblogNotification = microblogNotification;
	}

	@Override
	public MicroblogPostIdentifier getLatestPostIdentifier() throws MicroblogServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return microblogRevisionStorage.getLastRevision();
		} catch (final MicroblogRevisionStorageException e) {
			throw new MicroblogServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void mailPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final MicroblogPost microblogPost = microblogConnector.getPost(microblogPostIdentifier);
			microblogPostMailer.mailPost(microblogPost);
		} catch (final MicroblogPostNotifierException | MicroblogConnectorException e) {
			throw new MicroblogServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			microblogConversationMailer.mailConversation(microblogConversationIdentifier);
		} catch (final MicroblogConversationNotifierException e) {
			throw new MicroblogServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MicroblogConversationIdentifier createMicroblogConversationIdentifier(final long conversationNumber) {
		final Duration duration = durationUtil.getDuration();
		try {
			return new MicroblogConversationIdentifier(conversationNumber);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MicroblogPostIdentifier createMicroblogPostIdentifier(final long postNumber) {
		final Duration duration = durationUtil.getDuration();
		try {
			return new MicroblogPostIdentifier(postNumber);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MicroblogConversationIdentifier getMicroblogConversationIdentifierForPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return microblogConversationFinder.findIdentifier(microblogPostIdentifier);
		} catch (final MicroblogConnectorException | ParseException e) {
			throw new MicroblogServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void refreshPost(final SessionIdentifier sessionIdentifier) throws MicroblogServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			microblogPostRefresher.refresh();
		} catch (final AuthorizationServiceException e) {
			throw new MicroblogServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updatePost(final SessionIdentifier sessionIdentifier, final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			microblogPostUpdater.update(microblogPostIdentifier);
		} catch (final AuthorizationServiceException | MicroblogConnectorException e) {
			throw new MicroblogServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<String> listNotifications(final UserIdentifier userIdentifier) throws MicroblogServiceException {
		return microblogNotification.listNotifications(userIdentifier);
	}

	@Override
	public void activateNotification(final UserIdentifier userIdentifier, final String keyword) throws MicroblogServiceException {
		microblogNotification.activateNotification(userIdentifier, keyword);
	}

	@Override
	public void deactivateNotification(final UserIdentifier userIdentifier, final String keyword) throws MicroblogServiceException {
		microblogNotification.deactivateNotification(userIdentifier, keyword);
	}

}
