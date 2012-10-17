package de.benjaminborbe.lunch.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.lunch.guice.LunchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class LunchParseUtilIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LunchModulesMock());
		assertNotNull(injector.getInstance(LunchParseUtil.class));
	}

}
