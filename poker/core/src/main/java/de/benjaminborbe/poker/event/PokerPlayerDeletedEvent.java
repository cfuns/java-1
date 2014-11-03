package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public class PokerPlayerDeletedEvent extends Event<PokerPlayerDeletedEventHandler> {

	public static final Type<PokerPlayerDeletedEventHandler> TYPE = new Type<PokerPlayerDeletedEventHandler>();

	private final PokerPlayerIdentifier playerIdentifier;

	public PokerPlayerDeletedEvent(final PokerPlayerIdentifier playerIdentifier) {
		this.playerIdentifier = playerIdentifier;
	}

	@Override
	public Type<PokerPlayerDeletedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public void dispatch(final PokerPlayerDeletedEventHandler handler) throws Exception {
		handler.onPlayerDeleted(playerIdentifier);
	}
}
