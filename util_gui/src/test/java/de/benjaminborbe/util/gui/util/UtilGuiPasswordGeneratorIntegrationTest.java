package de.benjaminborbe.util.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.util.gui.guice.UtilGuiModulesMock;

public class UtilGuiPasswordGeneratorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilGuiModulesMock());
		final UtilGuiPasswordGenerator utilPasswordGenerator = injector.getInstance(UtilGuiPasswordGenerator.class);
		assertNotNull(utilPasswordGenerator);
	}

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilGuiModulesMock());
		final UtilGuiPasswordGenerator a = injector.getInstance(UtilGuiPasswordGenerator.class);
		final UtilGuiPasswordGenerator b = injector.getInstance(UtilGuiPasswordGenerator.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
