package de.benjaminborbe.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.util.guice.UtilModulesMock;

public class UtilActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new UtilModulesMock());
		final UtilActivator o = injector.getInstance(UtilActivator.class);
		assertNotNull(o);
	}

}
