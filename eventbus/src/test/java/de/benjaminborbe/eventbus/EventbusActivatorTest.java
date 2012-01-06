package de.benjaminborbe.eventbus;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventbusActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusActivator o = injector.getInstance(EventbusActivator.class);
		assertNotNull(o);
	}
}
