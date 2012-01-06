package de.benjaminborbe.slash;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SlashActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashActivator o = injector.getInstance(SlashActivator.class);
		assertNotNull(o);
	}

}
