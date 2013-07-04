package de.benjaminborbe.eventbus.gui.mock;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.Event.Type;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.eventbus.api.HandlerRegistration;

import java.util.List;
import java.util.Map;

public class EventbusServiceMock implements EventbusService {

	@Override
	public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
		return null;
	}

	@Override
	public <H extends EventHandler> void fireEvent(final Event<H> event) {
	}

	@Override
	public <H extends EventHandler> H getHandler(final Type<H> type, final int index) {
		return null;
	}

	@Override
	public int getHandlerCount(final Type<?> type) {
		return 0;
	}

	@Override
	public boolean isEventHandled(final Type<?> e) {
		return false;
	}

	@Override
	public Map<Type<EventHandler>, List<EventHandler>> getHandlers() {
		return null;
	}

}
