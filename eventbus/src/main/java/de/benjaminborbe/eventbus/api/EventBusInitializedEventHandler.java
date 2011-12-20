package de.benjaminborbe.eventbus.api;

public interface EventBusInitializedEventHandler extends EventHandler {

	public void onInitialize(EventBusInitializedEvent event);

}
