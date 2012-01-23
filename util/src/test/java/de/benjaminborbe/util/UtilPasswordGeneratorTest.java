package de.benjaminborbe.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.util.guice.UtilModulesMock;

public class UtilPasswordGeneratorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilModulesMock());
		final UtilPasswordGenerator utilPasswordGenerator = injector.getInstance(UtilPasswordGenerator.class);
		assertNotNull(utilPasswordGenerator);
	}

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilModulesMock());
		final UtilPasswordGenerator a = injector.getInstance(UtilPasswordGenerator.class);
		final UtilPasswordGenerator b = injector.getInstance(UtilPasswordGenerator.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void generatePassword() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final UtilPasswordGenerator utilPasswordGenerator = new UtilPasswordGeneratorImpl(logger);
		assertNotNull(utilPasswordGenerator);
		final String password = utilPasswordGenerator.generatePassword(1, UtilPasswordCharacter.NUMBER);
		assertNotNull(password);
		assertEquals(1, password.length());
	}

}
