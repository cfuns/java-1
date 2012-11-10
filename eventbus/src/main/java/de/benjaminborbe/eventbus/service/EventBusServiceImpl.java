package de.benjaminborbe.eventbus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.eventbus.api.aEvent;
import de.benjaminborbe.eventbus.api.aEvent.Type;
import de.benjaminborbe.eventbus.api.aEventHandler;
import de.benjaminborbe.eventbus.api.aEventbusService;
import de.benjaminborbe.eventbus.api.aHandlerRegistration;

@Singleton
public class EventbusServiceImpl implements aEventbusService {

	private final Map<Type<aEventHandler>, List<aEventHandler>> handlers = new HashMap<Type<aEventHandler>, List<aEventHandler>>();

	private final Logger logger;

	@Inject
	public EventbusServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends aEventHandler> aHandlerRegistration addHandler(final Type<H> type, final H handler) {
		logger.trace("CoreEventbus - register handler: " + handler.toString() + " for type: " + type.toString());
		List<aEventHandler> eventHandlers = handlers.get(type);
		if (eventHandlers == null) {
			eventHandlers = new ArrayList<aEventHandler>();
			handlers.put((Type<aEventHandler>) type, eventHandlers);
		}
		eventHandlers.add(handler);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <H extends aEventHandler> H getHandler(final Type<H> type, final int index) {
		final List<aEventHandler> eventHandlers = handlers.get(type);
		return (H) eventHandlers.get(index);
	}

	@Override
	public int getHandlerCount(final Type<?> type) {
		final List<aEventHandler> eventHandlers = handlers.get(type);
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
	public <H extends aEventHandler> void fireEvent(final aEvent<H> event) {
		logger.trace("CoreEventbus - event fired: " + event.getClass().getSimpleName() + ": " + event.toString());
		final List<aEventHandler> eventHandlers = handlers.get(event.getAssociatedType());
		if (eventHandlers != null) {
			for (final aEventHandler eventHandler : eventHandlers) {
				event.dispatch((H) eventHandler);
			}
		}
	}

	@Override
	public Map<Type<aEventHandler>, List<aEventHandler>> getHandlers() {
		return handlers;
	}

}
