package de.benjaminborbe.dhl.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.dhl.guice.DhlModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class DhlStatusParserIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new DhlModulesMock());
		assertNotNull(injector.getInstance(DhlStatusParser.class));
	}
}
