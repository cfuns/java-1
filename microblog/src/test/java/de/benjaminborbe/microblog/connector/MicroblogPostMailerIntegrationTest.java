package de.benjaminborbe.microblog.connector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.microblog.post.MicroblogPostNotifier;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogPostMailerIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogPostNotifier a = injector.getInstance(MicroblogPostNotifier.class);
		final MicroblogPostNotifier b = injector.getInstance(MicroblogPostNotifier.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}
}
