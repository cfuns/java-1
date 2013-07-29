package de.benjaminborbe.mail.mock;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MailServiceMock implements MailService {

	private final List<Mail> mails = new ArrayList<Mail>();

	@Inject
	public MailServiceMock() {
	}

	@Override
	public void send(final Mail mail) throws MailServiceException {
		mails.add(mail);
	}

	public List<Mail> getMails() {
		return mails;
	}

}
