package de.benjaminborbe.microblog.connector;

import com.google.inject.Injector;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MicroblogRevisionStorageIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogRevisionStorage a = injector.getInstance(MicroblogRevisionStorage.class);
		final MicroblogRevisionStorage b = injector.getInstance(MicroblogRevisionStorage.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
