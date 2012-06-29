package de.benjaminborbe.mail.service;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.util.MailSessionFactory;
import de.benjaminborbe.mail.util.MailSessionFactoryMock;

public class MailServiceUTF8UnitTest {

	@Ignore("mailserver")
	@Test
	public void testSendTestMail() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final MailSessionFactory mailSessionFactory = new MailSessionFactoryMock();
		final MailService mailService = new MailServiceUTF8(logger, mailSessionFactory);

		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Hällööö Wörld";
		final String content = "Toller UTF8-Contäääänt";
		final String contentType = "text/plain";
		final Mail mail = new Mail(from, to, subject, content, contentType);
		mailService.send(mail);
	}
}
