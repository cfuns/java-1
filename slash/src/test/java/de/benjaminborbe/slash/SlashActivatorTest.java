package de.benjaminborbe.slash;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.slash.SlashActivator;
import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SlashActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashActivator o = injector.getInstance(SlashActivator.class);
		assertNotNull(o);
	}
}
