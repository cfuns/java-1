package de.benjaminborbe.eventbus.api;

import java.util.List;
import java.util.Map;

import de.benjaminborbe.eventbus.api.Event.Type;

public interface EventbusService {

	<H extends EventHandler> HandlerRegistration addHandler(Type<H> type, H handler);

	<H extends EventHandler> void fireEvent(Event<H> event);

	<H extends EventHandler> H getHandler(Type<H> type, int index);

	int getHandlerCount(Type<?> type);

	boolean isEventHandled(Type<?> e);

	Map<Type<EventHandler>, List<EventHandler>> getHandlers();

}
