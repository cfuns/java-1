package de.benjaminborbe.eventbus.api;

import de.benjaminborbe.eventbus.api.Event.Type;

import java.util.List;
import java.util.Map;

public interface EventbusService {

	<H extends EventHandler> void addHandler(Type<H> type, H handler);

	<H extends EventHandler> void removeHandler(Type<H> type, H handler);

	<H extends EventHandler> void fireEvent(Event<H> event);

	Map<String, List<EventHandler>> getHandlers();

	int getHandlerCount(final Type<?> type);

	boolean isEventHandled(final Type<?> type);
}
