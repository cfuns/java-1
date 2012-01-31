package de.benjaminborbe.eventbus.api;

import de.benjaminborbe.eventbus.api.Event.Type;

public interface EventBusService {

	// normal
	<H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler);

	<H extends EventHandler> void fireEvent(Event<H> event);

	<H extends EventHandler> H getHandler(Type<H> type, int index);

	int getHandlerCount(Type<?> type);

	boolean isEventHandled(Type<?> e);

}
