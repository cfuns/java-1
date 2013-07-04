package de.benjaminborbe.microblog.conversation;

import com.google.inject.Injector;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MicroblogConversationMailerIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogConversationNotifier a = injector.getInstance(MicroblogConversationNotifier.class);
		final MicroblogConversationNotifier b = injector.getInstance(MicroblogConversationNotifier.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}
}
