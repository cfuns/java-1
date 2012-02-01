package de.benjaminborbe.eventbus.api;

public interface EventbusInitializedEventHandler extends EventHandler {

	public void onInitialize(EventbusInitializedEvent event);

}
