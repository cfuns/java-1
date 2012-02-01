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

public class EventBusServiceTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusService eventBus = injector.getInstance(EventbusService.class);
		assertNotNull(eventBus);
		assertEquals(EventBusServiceImpl.class, eventBus.getClass());
	}

	@Test
	public void EventBus() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventbusService eventBus = injector.getInstance(EventbusService.class);
		assertNotNull(eventBus);
		assertEquals(EventBusServiceImpl.class, eventBus.getClass());

		final List<EventbusInitializedEvent> events = new ArrayList<EventbusInitializedEvent>();

		assertEquals(0, eventBus.getHandlerCount(EventbusInitializedEvent.TYPE));
		assertFalse(eventBus.isEventHandled(EventbusInitializedEvent.TYPE));
		eventBus.addHandler(EventbusInitializedEvent.TYPE, new EventbusInitializedEventHandler() {

			@Override
			public void onInitialize(final EventbusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, eventBus.getHandlerCount(EventbusInitializedEvent.TYPE));
		assertTrue(eventBus.isEventHandled(EventbusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		eventBus.fireEvent(new EventbusInitializedEvent());
		assertEquals(1, events.size());
	}

}
