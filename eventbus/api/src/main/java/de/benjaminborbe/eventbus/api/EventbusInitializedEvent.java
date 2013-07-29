package de.benjaminborbe.eventbus.api;

public class EventbusInitializedEvent extends Event<EventbusInitializedEventHandler> {

	public static final Type<EventbusInitializedEventHandler> TYPE = new Type<EventbusInitializedEventHandler>();

	public EventbusInitializedEvent() {

	}

	@Override
	public void dispatch(final EventbusInitializedEventHandler handler) {
		handler.onInitialize(this);
	}

	@Override
	public Type<EventbusInitializedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
