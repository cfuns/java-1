package de.benjaminborbe.lunch.util;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;

public class LunchUserNotifierMail implements LunchUserNotifier {

	private final Logger logger;

	private final MailService mailService;

	@Inject
	public LunchUserNotifierMail(final Logger logger, final MailService mailService) {
		this.logger = logger;
		this.mailService = mailService;
	}

	@Override
	public void notify(final UserIdentifier userIdentifier, final String message) {
		try {
			logger.debug("notify user: " + userIdentifier);
			final String from = "bborbe@seibert-media.net";
			final String to = userIdentifier.getId() + "@seibert-media.net";
			final String subject = "Mittag ist da";
			final StringBuilder sb = new StringBuilder();
			sb.append("message: " + message);
			final String contentType = "text/plain";
			final MailDto mail = new MailDto(from, to, subject, sb.toString(), contentType);
			mailService.send(mail);
		}
		catch (final MailServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}
}
