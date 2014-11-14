package de.benjaminborbe.eventbus.api;

public interface EventHandler {

	<H extends EventHandler> Event.Type<H> getType();
}
