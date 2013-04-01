package de.benjaminborbe.notification.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.notification.api.Notification;
import de.benjaminborbe.notification.api.NotificationMediaIdentifier;
import de.benjaminborbe.notification.config.NotificationConfig;

public class NotificationNotifierMail implements NotificationNotifier {

	private static final NotificationMediaIdentifier TYPE = new NotificationMediaIdentifier("mail");

	private final Logger logger;

	private final MailService mailService;

	private final AuthenticationService authenticationService;

	private final NotificationConfig notificationConfig;

	@Inject
	public NotificationNotifierMail(final Logger logger, final MailService mailService, final AuthenticationService authenticationService, final NotificationConfig notificationConfig) {
		this.logger = logger;
		this.mailService = mailService;
		this.authenticationService = authenticationService;
		this.notificationConfig = notificationConfig;
	}

	@Override
	public void notify(final Notification notification) throws NotificationNotifierException {
		try {
			logger.debug("notify");
			final String from = getFrom(notification);
			final String to = getTo(notification);
			final String contentType = "text/plain";
			final MailDto mail = new MailDto(from, to, buildSubject(notification), buildMessage(notification), contentType);
			mailService.send(mail);
		}
		catch (final MailServiceException | AuthenticationServiceException e) {
			throw new NotificationNotifierException(e);
		}
	}

	private String getFrom(final Notification notification) throws AuthenticationServiceException {
		if (notification.getFrom() != null) {
			final User user = authenticationService.getUser(notification.getFrom());
			if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
				return user.getEmail();
			}
		}
		return notificationConfig.getEmailFrom();
	}

	private String getTo(final Notification notification) throws AuthenticationServiceException {
		final User user = authenticationService.getUser(notification.getTo());
		return user != null ? user.getEmail() : null;
	}

	private String buildSubject(final Notification notification) {
		if (notification.getSubject() != null && !notification.getSubject().trim().isEmpty()) {
			return notification.getSubject();
		}
		else {
			return notification.getMessage();
		}
	}

	private String buildMessage(final Notification notification) {
		if (notification.getMessage() != null && !notification.getMessage().trim().isEmpty()) {
			return notification.getMessage();
		}
		else {
			return notification.getSubject();
		}
	}

	@Override
	public NotificationMediaIdentifier getNotificationMediaIdentifier() {
		return TYPE;
	}
}
