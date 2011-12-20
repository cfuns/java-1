package de.benjaminborbe.eventbus;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.EventbusActivator;
import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventbusActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusActivator o = injector.getInstance(EventbusActivator.class);
		assertNotNull(o);
	}
}
