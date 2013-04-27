package de.benjaminborbe.microblog.post;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.notification.api.NotificationDto;
import de.benjaminborbe.notification.api.NotificationService;
import de.benjaminborbe.notification.api.NotificationServiceException;
import de.benjaminborbe.tools.util.StringUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MicroblogPostNotifierImpl implements MicroblogPostNotifier {

	private static final int SUBJECT_MAX_LENGTH = 80;

	private final Logger logger;

	private final NotificationService notificationService;

	private final StringUtil stringUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public MicroblogPostNotifierImpl(
		final Logger logger,
		final AuthenticationService authenticationService,
		final NotificationService notificationService,
		final StringUtil stringUtil
	) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.notificationService = notificationService;
		this.stringUtil = stringUtil;
	}

	@Override
	public void mailPost(final MicroblogPost rev) throws MicroblogPostNotifierException {
		try {
			logger.debug("mailConversation with rev " + rev);
			final NotificationDto notificationDto = buildNotificationDto(rev);
			notificationService.notify(notificationDto);
		} catch (final NotificationServiceException | MicroblogConnectorException | ValidationException | AuthenticationServiceException e) {
			throw new MicroblogPostNotifierException("MailSendException", e);
		}
	}

	protected NotificationDto buildNotificationDto(final MicroblogPost post) throws MicroblogConnectorException, AuthenticationServiceException {
		final StringBuffer mailContent = new StringBuffer();
		mailContent.append(post.getContent());
		mailContent.append("\n\n");
		mailContent.append(post.getConversationUrl() != null ? post.getConversationUrl() : post.getPostUrl());
		final String from = post.getAuthor();
		final String to = "bborbe";
		final String subject = "Micro: " + stringUtil.shorten(post.getContent(), SUBJECT_MAX_LENGTH);

		final NotificationDto notification = new NotificationDto();
		notification.setTo(authenticationService.createUserIdentifier(to));
		notification.setFrom(authenticationService.createUserIdentifier(from));
		notification.setSubject(subject);
		notification.setMessage(mailContent.toString());
		notification.setType(notificationService.createNotificationTypeIdentifier("microblog"));
		return notification;
	}

}
