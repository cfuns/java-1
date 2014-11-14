package de.benjaminborbe.eventbus.api;

public interface EventHandler {

	Event.Type<? extends EventHandler> getType();
}
