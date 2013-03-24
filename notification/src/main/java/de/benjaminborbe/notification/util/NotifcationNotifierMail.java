package de.benjaminborbe.notification.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.notification.config.NotificationConfig;

public class NotifcationNotifierMail implements NotifcationNotifier {

	private final Logger logger;

	private final MailService mailService;

	private final AuthenticationService authenticationService;

	private final NotificationConfig notificationConfig;

	@Inject
	public NotifcationNotifierMail(final Logger logger, final MailService mailService, final AuthenticationService authenticationService, final NotificationConfig notificationConfig) {
		this.logger = logger;
		this.mailService = mailService;
		this.authenticationService = authenticationService;
		this.notificationConfig = notificationConfig;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String subject, final String message) throws NotifcationNotifierException {
		try {
			logger.debug("notify");
			final User user = authenticationService.getUser(userIdentifier);
			final String from = notificationConfig.getEmailFrom();
			final String to = user.getEmail();
			final String contentType = "text/plain";
			final MailDto mail = new MailDto(from, to, subject, message, contentType);
			mailService.send(mail);
		}
		catch (final MailServiceException | AuthenticationServiceException e) {
			throw new NotifcationNotifierException(e);
		}
	}
}
