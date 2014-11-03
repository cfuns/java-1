package de.benjaminborbe.eventbus.mock;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.api.HandlerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventbusServiceMock implements EventbusService {

	private final Map<Type<EventHandler>, List<EventHandler>> handlers = new HashMap<Type<EventHandler>, List<EventHandler>>();

	public List<Event<? extends EventHandler>> getFiredEvents() {
		return firedEvents;
	}

	private final List<Event<? extends EventHandler>> firedEvents = new ArrayList<Event<? extends EventHandler>>();

	public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
		final List<EventHandler> hs = new ArrayList<EventHandler>();
		hs.add(handler);
		handlers.put((Type<EventHandler>) type, hs);
		return new HandlerRegistration() {

			@Override
			public void removeHandler() {
				handlers.remove(type);
			}
		};
	}

	@Override
	public <H extends EventHandler> void fireEvent(final Event<H> event) {
		firedEvents.add(event);
	}

	@Override
	public <H extends EventHandler> H getHandler(final Type<H> type, final int index) {
		return (H) handlers.get(type);
	}

	@Override
	public int getHandlerCount(final Type<?> type) {
		return handlers.containsKey(type) ? 1 : 0;
	}

	@Override
	public boolean isEventHandled(final Type<?> type) {
		return handlers.containsKey(type);
	}

	@Override
	public Map<Type<EventHandler>, List<EventHandler>> getHandlers() {
		return handlers;
	}

}
