package de.benjaminborbe.eventbus.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.api.aEventbusInitializedEvent;
import de.benjaminborbe.eventbus.api.aEventbusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.aEventbusService;
import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventbusServiceIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final aEventbusService Eventbus = injector.getInstance(aEventbusService.class);
		assertNotNull(Eventbus);
		assertEquals(EventbusServiceImpl.class, Eventbus.getClass());
	}

	@Test
	public void testEventbus() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final aEventbusService Eventbus = injector.getInstance(aEventbusService.class);
		assertNotNull(Eventbus);
		assertEquals(EventbusServiceImpl.class, Eventbus.getClass());

		final List<aEventbusInitializedEvent> events = new ArrayList<aEventbusInitializedEvent>();

		assertEquals(0, Eventbus.getHandlerCount(aEventbusInitializedEvent.TYPE));
		assertFalse(Eventbus.isEventHandled(aEventbusInitializedEvent.TYPE));
		Eventbus.addHandler(aEventbusInitializedEvent.TYPE, new aEventbusInitializedEventHandler() {

			@Override
			public void onInitialize(final aEventbusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, Eventbus.getHandlerCount(aEventbusInitializedEvent.TYPE));
		assertTrue(Eventbus.isEventHandled(aEventbusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		Eventbus.fireEvent(new aEventbusInitializedEvent());
		assertEquals(1, events.size());
	}

}
