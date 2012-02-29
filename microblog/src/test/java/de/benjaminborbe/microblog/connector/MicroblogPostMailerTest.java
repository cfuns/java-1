package de.benjaminborbe.microblog.connector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogPostMailerTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogPostMailer a = injector.getInstance(MicroblogPostMailer.class);
		final MicroblogPostMailer b = injector.getInstance(MicroblogPostMailer.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}
}
