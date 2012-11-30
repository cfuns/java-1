package de.benjaminborbe.util.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.util.gui.guice.UtilGuiModulesMock;

public class UtilGuiPasswordGeneratorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilGuiModulesMock());
		final PasswordGenerator utilPasswordGenerator = injector.getInstance(PasswordGenerator.class);
		assertNotNull(utilPasswordGenerator);
	}

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilGuiModulesMock());
		final PasswordGenerator a = injector.getInstance(PasswordGenerator.class);
		final PasswordGenerator b = injector.getInstance(PasswordGenerator.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

}
