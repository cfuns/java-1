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

	private final Map<String, List<EventHandler>> handlers = new HashMap<String, List<EventHandler>>();

	private final Logger logger;

	@Inject
	public EventbusServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends EventHandler> void addHandler(final Type<H> type, final H handler) {
		logger.debug("add handler: {} for type: {}", handler, type);

		final List<EventHandler> eventHandlers;
		if (handlers.containsKey(type.toString())) {
			eventHandlers = handlers.get(type.toString());
		} else {
			eventHandlers = new ArrayList<EventHandler>();
			handlers.put(type.toString(), eventHandlers);
		}

		eventHandlers.add(handler);
	}

	@Override
	public <H extends EventHandler> void removeHandler(final Type<H> type, final H handler) {
		logger.debug("remove handler: {} for type: {}", handler, type);

		final List<EventHandler> eventHandlers;
		if (handlers.containsKey(type.toString())) {
			eventHandlers = handlers.get(type.toString());
		} else {
			eventHandlers = new ArrayList<EventHandler>();
			handlers.put(type.toString(), eventHandlers);
		}

		eventHandlers.remove(handler);
	}

	@SuppressWarnings("unchecked")
	public <H extends EventHandler> H getHandler(final Type<H> type, final int index) {
		final List<EventHandler> eventHandlers = handlers.get(type.toString());
		return (H) eventHandlers.get(index);
	}

	public int getHandlerCount(final Type<?> type) {
		final List<EventHandler> eventHandlers = handlers.get(type.toString());
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
		logger.trace("event fired: " + event.getClass().getSimpleName() + ": " + event.toString());
		final List<EventHandler> eventHandlers = handlers.get(event.getAssociatedType().toString());
		if (eventHandlers == null || eventHandlers.isEmpty()) {
			logger.debug("no eventhandler found for type: {}", event.getAssociatedType());
		} else {
			for (final EventHandler eventHandler : eventHandlers) {
				try {
					event.dispatch((H) eventHandler);
				} catch (Exception e) {
					logger.debug("dispatch event failed", e);
				}
			}
		}
	}

	public Map<String, List<EventHandler>> getHandlers() {
		return handlers;
	}

}
