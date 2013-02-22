package de.benjaminborbe.microblog.connector;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogConnectorImplIntegrationTest {

	@Test
	public void testGetPost() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogConnector microblogConnector = injector.getInstance(MicroblogConnector.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);

		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(31697l);
		final MicroblogPostResult post = microblogConnector.getPost(microblogPostIdentifier);
		assertNotNull(post);
		assertNotNull(post.getDate());
		assertEquals("2013-02-22 08:22:33", calendarUtil.toDateTimeString(post.getDate()));
	}
}
