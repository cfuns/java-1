package de.benjaminborbe.mail.util;

import com.google.inject.Provider;
import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperString;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MailJsonMapperUnitTest {

	@Test
	public void testName() throws Exception {
		final Provider<MailDto> a = new ProviderMock<>(MailDto.class);
		final MapperString b = new MapperString();
		final MailJsonMapper mapper = new MailJsonMapper(a, b);

		final String from = "a";
		final String to = "b";
		final String subject = "c";
		final String content = "d";
		final String contentType = "e";

		final MailDto mail = new MailDto();
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setContent(content);
		mail.setContentType(contentType);

		final String json = mapper.map(mail);
		assertNotNull(json);

		final MailDto mail2 = mapper.map(json);
		assertEquals(from, mail2.getFrom());
		assertEquals(to, mail2.getTo());
		assertEquals(subject, mail2.getSubject());
		assertEquals(content, mail2.getContent());
		assertEquals(contentType, mail2.getContentType());
	}
}
