package de.benjaminborbe.eventbus.mock;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventbusServiceMock implements EventbusService {

	private final Map<Type<EventHandler>, List<EventHandler>> handlers = new HashMap<Type<EventHandler>, List<EventHandler>>();

	private final List<Event<? extends EventHandler>> firedEvents = new ArrayList<Event<? extends EventHandler>>();

	public List<Event<? extends EventHandler>> getFiredEvents() {
		return firedEvents;
	}

	public <H extends EventHandler> void addHandler(final Type<H> type, final H handler) {
		final List<EventHandler> hs = new ArrayList<EventHandler>();
		hs.add(handler);
		handlers.put((Type<EventHandler>) type, hs);
	}

	@Override
	public <H extends EventHandler> void removeHandler(final Type<H> type, final H handler) {
		handlers.remove(type);
	}

	@Override
	public <H extends EventHandler> void fireEvent(final Event<H> event) {
		firedEvents.add(event);
	}

	@Override
	public Map<Type<EventHandler>, List<EventHandler>> getHandlers() {
		return handlers;
	}

	public int getHandlerCount(final Type<?> type) {
		final List<EventHandler> eventHandlers = handlers.get(type);
		if (eventHandlers != null) {
			return eventHandlers.size();
		}
		return 0;
	}

	public boolean isEventHandled(final Type<?> type) {
		return getHandlerCount(type) > 0;
	}
}
