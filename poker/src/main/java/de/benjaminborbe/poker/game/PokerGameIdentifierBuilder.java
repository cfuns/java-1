package de.benjaminborbe.poker.game;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.poker.api.PokerGameIdentifier;

public class PokerGameIdentifierBuilder implements IdentifierBuilder<String, PokerGameIdentifier> {

	@Override
	public PokerGameIdentifier buildIdentifier(final String value) {
		return new PokerGameIdentifier(value);
	}

}
