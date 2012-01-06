package de.benjaminborbe.navigation;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.navigation.guice.NavigationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class NavigationActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NavigationModulesMock());
		final NavigationActivator o = injector.getInstance(NavigationActivator.class);
		assertNotNull(o);
	}

}
