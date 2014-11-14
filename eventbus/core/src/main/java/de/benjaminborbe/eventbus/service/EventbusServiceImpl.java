package de.benjaminborbe.eventbus.service;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class EventbusServiceImpl implements EventbusService {

	private final Map<Type<EventHandler>, List<EventHandler>> handlers = new HashMap<Type<EventHandler>, List<EventHandler>>();

	private final Logger logger;

	@Inject
	public EventbusServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends EventHandler> void addHandler(final Type<H> type, final H handler) {
		logger.trace("CoreEventbus - add handler: " + handler.toString() + " for type: " + type.toString());

		final List<EventHandler> eventHandlers;
		if (handlers.containsKey(type)) {
			eventHandlers = handlers.get(type);
		} else {
			eventHandlers = new ArrayList<EventHandler>();
			handlers.put((Type<EventHandler>) type, eventHandlers);
		}

		eventHandlers.add(handler);
	}

	@Override
	public <H extends EventHandler> void removeHandler(final Type<H> type, final H handler) {
		logger.trace("CoreEventbus - remove handler: " + handler.toString() + " for type: " + type.toString());

		final List<EventHandler> eventHandlers;
		if (handlers.containsKey(type)) {
			eventHandlers = handlers.get(type);
		} else {
			eventHandlers = new ArrayList<EventHandler>();
			handlers.put((Type<EventHandler>) type, eventHandlers);
		}

		eventHandlers.remove(handler);
	}

	@SuppressWarnings("unchecked")
	public <H extends EventHandler> H getHandler(final Type<H> type, final int index) {
		final List<EventHandler> eventHandlers = handlers.get(type);
		return (H) eventHandlers.get(index);
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

	@SuppressWarnings("unchecked")
	@Override
	public <H extends EventHandler> void fireEvent(final Event<H> event) {
		logger.trace("CoreEventbus - event fired: " + event.getClass().getSimpleName() + ": " + event.toString());
		final List<EventHandler> eventHandlers = handlers.get(event.getAssociatedType());
		if (eventHandlers != null) {
			for (final EventHandler eventHandler : eventHandlers) {
				try {
					event.dispatch((H) eventHandler);
				} catch (Exception e) {
					logger.debug("dispatch event failed", e);
				}
			}
		}
	}

	public Map<Type<EventHandler>, List<EventHandler>> getHandlers() {
		return handlers;
	}

}
