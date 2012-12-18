package de.benjaminborbe.mail.mock;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;

public class MailServiceMock implements MailService {

	@Override
	public void send(final Mail mail) throws MailSendException {
	}

}
