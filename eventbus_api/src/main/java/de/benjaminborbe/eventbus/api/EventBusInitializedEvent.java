package de.benjaminborbe.eventbus.api;

public class EventBusInitializedEvent extends Event<EventBusInitializedEventHandler> {

	public static Type<EventBusInitializedEventHandler> TYPE = new Type<EventBusInitializedEventHandler>();

	public EventBusInitializedEvent() {

	}

	@Override
	public void dispatch(EventBusInitializedEventHandler handler) {
		handler.onInitialize(this);
	}

	@Override
	public Type<EventBusInitializedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
