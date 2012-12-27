package de.benjaminborbe.mail.mock;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;

@Singleton
public class MailServiceMock implements MailService {

	private final List<Mail> mails = new ArrayList<Mail>();

	@Override
	public void send(final Mail mail) throws MailSendException {
		mails.add(mail);
	}

	public List<Mail> getMails() {
		return mails;
	}

}
