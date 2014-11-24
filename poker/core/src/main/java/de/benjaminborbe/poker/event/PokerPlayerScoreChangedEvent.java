package de.benjaminborbe.poker.event;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public class PokerPlayerScoreChangedEvent extends Event<PokerPlayerScoreChangedEventHandler> {

	public final static Event.Type<PokerPlayerScoreChangedEventHandler> TYPE = new Event.Type<PokerPlayerScoreChangedEventHandler>(PokerPlayerScoreChangedEventHandler.class);

	private final PokerPlayerIdentifier pokerPlayerIdentifier;

	private final Long score;

	public PokerPlayerScoreChangedEvent(final PokerPlayerIdentifier pokerPlayerIdentifier, final Long score) {
		this.pokerPlayerIdentifier = pokerPlayerIdentifier;
		this.score = score;
	}

	public Long getScore() {
		return score;
	}

	public PokerPlayerIdentifier getPokerPlayerIdentifier() {
		return pokerPlayerIdentifier;
	}

	@Override
	public Type<PokerPlayerScoreChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public void dispatch(final PokerPlayerScoreChangedEventHandler handler) {
		handler.onScoreChanged(getPokerPlayerIdentifier(), getScore());
	}

}
