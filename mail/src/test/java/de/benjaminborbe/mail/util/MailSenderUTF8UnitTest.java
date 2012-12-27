package de.benjaminborbe.mail.util;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.mail.api.MailDto;

public class MailSenderUTF8UnitTest {

	@Ignore("mailserver")
	@Test
	public void testSendTestMail() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final MailSessionFactory mailSessionFactory = new MailSessionFactoryMock();
		final MailSender mailService = new MailSenderUTF8(logger, mailSessionFactory);

		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Hällööö Wörld";
		final String content = "Toller UTF8-Contäääänt";
		final String contentType = "text/plain";
		final MailDto mail = new MailDto(from, to, subject, content, contentType);
		mailService.send(mail);
	}
}
