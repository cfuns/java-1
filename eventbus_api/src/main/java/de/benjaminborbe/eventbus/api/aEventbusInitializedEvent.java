package de.benjaminborbe.eventbus.api;

public class aEventbusInitializedEvent extends aEvent<aEventbusInitializedEventHandler> {

	public static Type<aEventbusInitializedEventHandler> TYPE = new Type<aEventbusInitializedEventHandler>();

	public aEventbusInitializedEvent() {

	}

	@Override
	public void dispatch(aEventbusInitializedEventHandler handler) {
		handler.onInitialize(this);
	}

	@Override
	public Type<aEventbusInitializedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
