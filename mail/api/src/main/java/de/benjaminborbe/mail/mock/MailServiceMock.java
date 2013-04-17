package de.benjaminborbe.mail.mock;

import com.google.inject.Singleton;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MailServiceMock implements MailService {

	private final List<Mail> mails = new ArrayList<>();

	@Override
	public void send(final Mail mail) throws MailServiceException {
		mails.add(mail);
	}

	public List<Mail> getMails() {
		return mails;
	}

}
