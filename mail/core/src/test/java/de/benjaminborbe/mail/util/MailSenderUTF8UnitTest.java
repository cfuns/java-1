package de.benjaminborbe.mail.util;

import de.benjaminborbe.mail.api.MailDto;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

public class MailSenderUTF8UnitTest {

	@Ignore("mailserver")
	@Test
	public void testSendTestMail() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final MailSessionFactory mailSessionFactory = new MailSessionFactoryMock();
		final MailSender mailService = new MailSenderUTF8(logger, mailSessionFactory);

		final String from = "foo@example.com";
		final String to = "foo@example.com";
		final String subject = "Hällööö Wörld";
		final String content = "Toller UTF8-Contäääänt";
		final String contentType = "text/plain";
		final MailDto mail = new MailDto(from, to, subject, content, contentType);
		mailService.send(mail);
	}
}
