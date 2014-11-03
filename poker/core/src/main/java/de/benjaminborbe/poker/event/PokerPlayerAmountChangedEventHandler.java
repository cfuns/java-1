package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public interface PokerPlayerAmountChangedEventHandler extends EventHandler {

	void onAmountChanged(PokerPlayerIdentifier pokerPlayerIdentifier, Long amount);
}
