package de.benjaminborbe.poker.player;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public class PokerPlayerIdentifierBuilder implements IdentifierBuilder<String, PokerPlayerIdentifier> {

	@Override
	public PokerPlayerIdentifier buildIdentifier(final String value) {
		return new PokerPlayerIdentifier(value);
	}

}
