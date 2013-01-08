package de.benjaminborbe.mail.mock;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;

@Singleton
public class MailServiceMock implements MailService {

	private final List<Mail> mails = new ArrayList<Mail>();

	@Override
	public void send(final Mail mail) throws MailServiceException {
		mails.add(mail);
	}

	public List<Mail> getMails() {
		return mails;
	}

}
