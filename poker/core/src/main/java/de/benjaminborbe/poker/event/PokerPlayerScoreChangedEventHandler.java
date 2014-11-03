package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public interface PokerPlayerScoreChangedEventHandler extends EventHandler {

	void onScoreChanged(final PokerPlayerIdentifier id, final Long score);
}
