package de.benjaminborbe.eventbus.service;

import com.google.inject.Injector;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventbusServiceIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusService Eventbus = injector.getInstance(EventbusService.class);
		assertNotNull(Eventbus);
		assertEquals(EventbusServiceImpl.class, Eventbus.getClass());
	}

}
