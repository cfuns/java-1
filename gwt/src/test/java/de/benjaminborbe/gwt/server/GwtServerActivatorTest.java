package de.benjaminborbe.gwt.server;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.gwt.server.guice.GwtServerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class GwtServerActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GwtServerModulesMock());
		final GwtServerActivator o = injector.getInstance(GwtServerActivator.class);
		assertNotNull(o);
	}
}
