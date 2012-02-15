package de.benjaminborbe.util.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.util.gui.guice.UtilGuiModulesMock;

public class UtilGuiPasswordGeneratorTest {

	@Test
	public void testinject() {
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

	@Test
	public void testgeneratePassword() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final UtilGuiPasswordGenerator utilPasswordGenerator = new UtilGuiPasswordGeneratorImpl(logger);
		assertNotNull(utilPasswordGenerator);
		final String password = utilPasswordGenerator.generatePassword(1, UtilGuiPasswordCharacter.NUMBER);
		assertNotNull(password);
		assertEquals(1, password.length());
	}

}
