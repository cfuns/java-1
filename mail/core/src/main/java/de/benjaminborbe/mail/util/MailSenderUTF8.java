package de.benjaminborbe.mail.util;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailServiceException;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSenderUTF8 implements MailSender {

	private final Logger logger;

	private final MailSessionFactory mailSessionFactory;

	@Inject
	public MailSenderUTF8(final Logger logger, final MailSessionFactory mailSessionFactory) {
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
			final String charset = "utf-8";
			final MimeMessage message = new MimeMessage(mailSessionFactory.getInstance());
			message.setFrom(new InternetAddress(mail.getFrom()));
			final InternetAddress recipients[] = {new InternetAddress(mail.getTo())};
			message.setRecipients(Message.RecipientType.TO, recipients);
			message.setSubject(mail.getSubject(), charset);
			final BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mail.getContent(), mail.getContentType() + "; charset=" + charset);
			final Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
		} catch (final Exception e) {
			throw new MailServiceException(e.getClass().getSimpleName(), e);
		}
	}
}
