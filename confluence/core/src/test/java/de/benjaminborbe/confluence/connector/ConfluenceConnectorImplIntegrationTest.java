package de.benjaminborbe.confluence.connector;

import com.google.inject.Injector;
import de.benjaminborbe.confluence.guice.ConfluenceModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfluenceConnectorImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfluenceModulesMock());
		assertEquals(ConfluenceConnectorImpl.class, injector.getInstance(ConfluenceConnector.class).getClass());
	}

}
