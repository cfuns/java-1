package de.benjaminborbe.microblog.conversation;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.notification.api.NotificationDto;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.tools.util.StringUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class MicroblogConversationNotifierImpl implements MicroblogConversationNotifier {

	private static final int SUBJECT_MAX_LENGTH = 80;

	private final Logger logger;

	private final NotificationService notificationService;

	private final MicroblogConnector microblogConnector;

	private final StringUtil stringUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public MicroblogConversationNotifierImpl(
		final Logger logger,
		final AuthenticationService authenticationService,
		final MicroblogConnector microblogConnector,
		final NotificationService notificationService,
		final StringUtil stringUtil
	) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.microblogConnector = microblogConnector;
		this.notificationService = notificationService;
		this.stringUtil = stringUtil;
	}

	@Override
	public void mailConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogConversationNotifierException {
		try {
			logger.debug("mailConversation with rev " + microblogConversationIdentifier);
			final NotificationDto notification = buildNotificationDto(microblogConversationIdentifier);
			notificationService.notify(notification);
		} catch (final MicroblogConnectorException | NotificationServiceException | ValidationException | AuthenticationServiceException e) {
			logger.error("MicroblogConnectorException", e);
			throw new MicroblogConversationNotifierException(e.getClass().getSimpleName(), e);
		}
	}

	private NotificationDto buildNotificationDto(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogConnectorException,
		AuthenticationServiceException {
		final MicroblogConversationResult microblogConversationResult = microblogConnector.getConversation(microblogConversationIdentifier);

		final List<MicroblogPostResult> posts = microblogConversationResult.getPosts();
		if (posts == null || posts.size() == 0) {
			throw new MicroblogConnectorException("no posts found");
		}
		final MicroblogPostResult firstPost = posts.get(0);
		final MicroblogPostResult lastPost = posts.get(posts.size() - 1);
		final StringBuffer mailContent = new StringBuffer();
		for (final MicroblogPostResult post : posts) {
			mailContent.append(post.getAuthor() + ": " + post.getContent());
			mailContent.append("\n\n");
		}
		mailContent.append(microblogConversationResult.getConversationUrl());
		final String from = lastPost.getAuthor();
		final String to = "bborbe";
		final String subject = "Micro: " + stringUtil.shorten(firstPost.getContent(), SUBJECT_MAX_LENGTH);

		final NotificationDto notification = new NotificationDto();
		notification.setTo(authenticationService.createUserIdentifier(to));
		notification.setFrom(authenticationService.createUserIdentifier(from));
		notification.setSubject(subject);
		notification.setMessage(mailContent.toString());
		notification.setType(notificationService.createNotificationTypeIdentifier("microblog"));
		return notification;
	}
}
