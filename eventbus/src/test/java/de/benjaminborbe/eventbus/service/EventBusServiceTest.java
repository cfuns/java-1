package de.benjaminborbe.eventbus.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.eventbus.api.EventBusInitializedEvent;
import de.benjaminborbe.eventbus.api.EventBusInitializedEventHandler;
import de.benjaminborbe.eventbus.api.EventBusService;
import de.benjaminborbe.eventbus.guice.EventbusModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class EventBusServiceTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventBusService eventBus = injector.getInstance(EventBusService.class);
		assertNotNull(eventBus);
		assertEquals(EventBusServiceImpl.class, eventBus.getClass());
	}

	@Test
	public void EventBus() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new EventbusModulesMock());
		final EventBusService eventBus = injector.getInstance(EventBusService.class);
		assertNotNull(eventBus);
		assertEquals(EventBusServiceImpl.class, eventBus.getClass());

		final List<EventBusInitializedEvent> events = new ArrayList<EventBusInitializedEvent>();

		assertEquals(0, eventBus.getHandlerCount(EventBusInitializedEvent.TYPE));
		assertFalse(eventBus.isEventHandled(EventBusInitializedEvent.TYPE));
		eventBus.addHandler(EventBusInitializedEvent.TYPE, new EventBusInitializedEventHandler() {

			@Override
			public void onInitialize(final EventBusInitializedEvent event) {
				events.add(event);
			}
		});
		assertEquals(1, eventBus.getHandlerCount(EventBusInitializedEvent.TYPE));
		assertTrue(eventBus.isEventHandled(EventBusInitializedEvent.TYPE));
		assertEquals(0, events.size());
		eventBus.fireEvent(new EventBusInitializedEvent());
		assertEquals(1, events.size());
	}

}
