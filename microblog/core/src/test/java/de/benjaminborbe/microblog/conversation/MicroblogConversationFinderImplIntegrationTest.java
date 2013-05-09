package de.benjaminborbe.microblog.conversation;

import com.google.inject.Injector;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MicroblogConversationFinderImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		assertNotNull(injector.getInstance(MicroblogConversationFinder.class));
	}

}
