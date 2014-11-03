package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public class PokerPlayerCreatedEvent extends Event<PokerPlayerCreatedEventHandler> {

	public static final Type<PokerPlayerCreatedEventHandler> TYPE = new Type<PokerPlayerCreatedEventHandler>();

	private final PokerPlayerIdentifier pokerPlayerIdentifier;

	public PokerPlayerCreatedEvent(final PokerPlayerIdentifier pokerPlayerIdentifier) {
		this.pokerPlayerIdentifier = pokerPlayerIdentifier;
	}

	public PokerPlayerIdentifier getPokerPlayerIdentifier() {
		return pokerPlayerIdentifier;
	}

	@Override
	public Type<PokerPlayerCreatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public void dispatch(final PokerPlayerCreatedEventHandler handler) {
		handler.onPlayerCreated(this);
	}
}
