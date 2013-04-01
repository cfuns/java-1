package de.benjaminborbe.mail.util;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailServiceException;

public interface MailSender {

	void send(Mail mail) throws MailServiceException;

}
