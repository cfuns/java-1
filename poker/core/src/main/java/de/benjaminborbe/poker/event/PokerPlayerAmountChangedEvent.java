package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public class PokerPlayerAmountChangedEvent extends Event<PokerPlayerAmountChangedEventHandler> {

	public static final Type<PokerPlayerAmountChangedEventHandler> TYPE = new Type<PokerPlayerAmountChangedEventHandler>();

	private final PokerPlayerIdentifier pokerPlayerIdentifier;

	private final Long amount;

	public PokerPlayerAmountChangedEvent(final PokerPlayerIdentifier pokerPlayerIdentifier, final Long amount) {
		this.pokerPlayerIdentifier = pokerPlayerIdentifier;
		this.amount = amount;
	}

	@Override
	public Type<PokerPlayerAmountChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public void dispatch(final PokerPlayerAmountChangedEventHandler handler) {
		handler.onAmountChanged(pokerPlayerIdentifier, amount);
	}
}
