package de.benjaminborbe.eventbus.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.api.EventbusInitializedEvent;
import de.benjaminborbe.eventbus.api.EventbusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventbusServiceTest {

	@Test
	public void testinject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusService Eventbus = injector.getInstance(EventbusService.class);
		assertNotNull(Eventbus);
		assertEquals(EventbusServiceImpl.class, Eventbus.getClass());
	}

	@Test
	public void testEventbus() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusService Eventbus = injector.getInstance(EventbusService.class);
		assertNotNull(Eventbus);
		assertEquals(EventbusServiceImpl.class, Eventbus.getClass());

		final List<EventbusInitializedEvent> events = new ArrayList<EventbusInitializedEvent>();

		assertEquals(0, Eventbus.getHandlerCount(EventbusInitializedEvent.TYPE));
		assertFalse(Eventbus.isEventHandled(EventbusInitializedEvent.TYPE));
		Eventbus.addHandler(EventbusInitializedEvent.TYPE, new EventbusInitializedEventHandler() {

			@Override
			public void onInitialize(final EventbusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, Eventbus.getHandlerCount(EventbusInitializedEvent.TYPE));
		assertTrue(Eventbus.isEventHandled(EventbusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		Eventbus.fireEvent(new EventbusInitializedEvent());
		assertEquals(1, events.size());
	}

}
