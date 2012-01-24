package de.benjaminborbe.mail.service;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.util.MailSessionFactory;

@Singleton
public class MailServiceDefault implements MailService {

	private final Logger logger;

	private final MailSessionFactory mailSessionFactory;

	@Inject
	public MailServiceDefault(final Logger logger, final MailSessionFactory mailSessionFactory) {
		this.logger = logger;
		this.mailSessionFactory = mailSessionFactory;
	}

	@Override
	public void send(final Mail mail) throws MailSendException {
		try {
			logger.debug("send mail to " + mail.getTo());
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
		}
		catch (final Exception e) {
			throw new MailSendException(e.getClass().getSimpleName(), e);
		}
	}
}
