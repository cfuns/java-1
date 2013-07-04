package de.benjaminborbe.microblog.service;

import com.google.inject.Injector;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MicroblogCronJobIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogCronJob a = injector.getInstance(MicroblogCronJob.class);
		final MicroblogCronJob b = injector.getInstance(MicroblogCronJob.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
