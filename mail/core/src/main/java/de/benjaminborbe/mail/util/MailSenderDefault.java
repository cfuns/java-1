package de.benjaminborbe.mail.util;

import com.google.inject.Inject;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailServiceException;
import org.slf4j.Logger;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSenderDefault implements MailSender {

	private final Logger logger;

	private final MailSessionFactory mailSessionFactory;

	@Inject
	public MailSenderDefault(final Logger logger, final MailSessionFactory mailSessionFactory) {
		this.logger = logger;
		this.mailSessionFactory = mailSessionFactory;
	}

	@Override
	public void send(final Mail mail) throws MailServiceException {
		if (mail == null) {
			throw new MailServiceException("parameter mail missing");
		}
		if (mail.getTo() != null && mail.getTo().contains("@example.com")) {
			logger.debug("skip send mail because @example.com - subject: " + mail.getSubject());
			return;
		}
		try {
			logger.trace("send mail to " + mail.getTo());
			final String from = mail.getFrom();
			final String to = mail.getTo();
			final String subject = mail.getSubject();
			final String content = mail.getContent();
			final Message message = new MimeMessage(mailSessionFactory.getInstance());
			message.setFrom(new InternetAddress(from));
			final InternetAddress recipients[] = new InternetAddress[1];
			recipients[0] = new InternetAddress(to);
			message.setRecipients(Message.RecipientType.TO, recipients);
			message.setSubject(subject);
			message.setContent(content, "text/plain");
			Transport.send(message);
		} catch (final Exception e) {
			throw new MailServiceException(e.getClass().getSimpleName(), e);
		}
	}
}
