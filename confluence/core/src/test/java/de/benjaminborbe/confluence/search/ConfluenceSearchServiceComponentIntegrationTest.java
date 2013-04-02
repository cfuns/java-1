package de.benjaminborbe.confluence.search;

import com.google.inject.Injector;
import de.benjaminborbe.confluence.guice.ConfluenceModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfluenceSearchServiceComponentIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		assertEquals(ConfluenceSearchServiceComponent.class, injector.getInstance(ConfluenceSearchServiceComponent.class).getClass());
	}
}
