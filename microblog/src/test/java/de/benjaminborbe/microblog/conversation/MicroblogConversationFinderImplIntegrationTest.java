package de.benjaminborbe.microblog.conversation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnectorException;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogConversationFinderImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		assertNotNull(injector.getInstance(MicroblogConversationFinder.class));
	}

	@Test
	public void testExtract() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogConversationFinder microblogConversationFinder = injector.getInstance(MicroblogConversationFinder.class);
		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(22279l);
		final MicroblogConversationIdentifier microblogConversationIdentifier = microblogConversationFinder.findIdentifier(microblogPostIdentifier);
		assertNotNull(microblogConversationIdentifier);
		assertEquals(new Long(15513l), microblogConversationIdentifier.getId());
	}

	@Test
	public void testNoConversation() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogConversationFinder microblogConversationFinder = injector.getInstance(MicroblogConversationFinder.class);
		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(20006l);
		assertNull(microblogConversationFinder.findIdentifier(microblogPostIdentifier));
	}

	@Test(expected = MicroblogConnectorException.class)
	public void testPostNotExists() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogConversationFinder microblogConversationFinder = injector.getInstance(MicroblogConversationFinder.class);
		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(6666666l);
		microblogConversationFinder.findIdentifier(microblogPostIdentifier);
	}
}
