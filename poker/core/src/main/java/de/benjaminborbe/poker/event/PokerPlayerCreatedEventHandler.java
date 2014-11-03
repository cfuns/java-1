package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.EventHandler;

public interface PokerPlayerCreatedEventHandler extends EventHandler {

	void onPlayerCreated(PokerPlayerCreatedEvent event);
}
