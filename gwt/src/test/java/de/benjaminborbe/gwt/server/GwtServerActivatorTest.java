package de.benjaminborbe.gwt.server;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.gwt.server.guice.GwtServerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GwtServerActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GwtServerModulesMock());
		final GwtServerActivator o = injector.getInstance(GwtServerActivator.class);
		assertNotNull(o);
	}
}
