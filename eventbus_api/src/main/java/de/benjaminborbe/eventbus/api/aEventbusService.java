package de.benjaminborbe.eventbus.api;

import java.util.List;
import java.util.Map;

import de.benjaminborbe.eventbus.api.aEvent.Type;

public interface aEventbusService {

	<H extends aEventHandler> aHandlerRegistration addHandler(Type<H> type, H handler);

	<H extends aEventHandler> void fireEvent(aEvent<H> event);

	<H extends aEventHandler> H getHandler(Type<H> type, int index);

	int getHandlerCount(Type<?> type);

	boolean isEventHandled(Type<?> e);

	Map<Type<aEventHandler>, List<aEventHandler>> getHandlers();

}
