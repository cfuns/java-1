package de.benjaminborbe.eventbus.gui.mock;

import java.util.List;
import java.util.Map;

import de.benjaminborbe.eventbus.api.aEvent;
import de.benjaminborbe.eventbus.api.aEvent.Type;
import de.benjaminborbe.eventbus.api.aEventHandler;
import de.benjaminborbe.eventbus.api.aEventbusService;
import de.benjaminborbe.eventbus.api.aHandlerRegistration;

public class aEventbusServiceMock implements aEventbusService {

	@Override
	public <H extends aEventHandler> aHandlerRegistration addHandler(final Type<H> type, final H handler) {
		return null;
	}

	@Override
	public <H extends aEventHandler> void fireEvent(final aEvent<H> event) {
	}

	@Override
	public <H extends aEventHandler> H getHandler(final Type<H> type, final int index) {
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
	public Map<Type<aEventHandler>, List<aEventHandler>> getHandlers() {
		return null;
	}

}
