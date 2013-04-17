package de.benjaminborbe.eventbus.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.api.HandlerRegistration;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EventbusServiceImpl implements EventbusService {

	private final Map<Type<EventHandler>, List<EventHandler>> handlers = new HashMap<>();

	private final Logger logger;

	@Inject
	public EventbusServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
		logger.trace("CoreEventbus - register handler: " + handler.toString() + " for type: " + type.toString());
		List<EventHandler> eventHandlers = handlers.get(type);
		if (eventHandlers == null) {
			eventHandlers = new ArrayList<>();
			handlers.put((Type<EventHandler>) type, eventHandlers);
		}
		eventHandlers.add(handler);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends EventHandler> H getHandler(final Type<H> type, final int index) {
		final List<EventHandler> eventHandlers = handlers.get(type);
		return (H) eventHandlers.get(index);
	}

	@Override
	public int getHandlerCount(final Type<?> type) {
		final List<EventHandler> eventHandlers = handlers.get(type);
		if (eventHandlers != null) {
			return eventHandlers.size();
		}
		return 0;
	}

	@Override
	public boolean isEventHandled(final Type<?> type) {
		return getHandlerCount(type) > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends EventHandler> void fireEvent(final Event<H> event) {
		logger.trace("CoreEventbus - event fired: " + event.getClass().getSimpleName() + ": " + event.toString());
		final List<EventHandler> eventHandlers = handlers.get(event.getAssociatedType());
		if (eventHandlers != null) {
			for (final EventHandler eventHandler : eventHandlers) {
				event.dispatch((H) eventHandler);
			}
		}
	}

	@Override
	public Map<Type<EventHandler>, List<EventHandler>> getHandlers() {
		return handlers;
	}

}
