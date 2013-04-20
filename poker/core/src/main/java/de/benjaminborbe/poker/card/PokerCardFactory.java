package de.benjaminborbe.poker.card;

import javax.inject.Inject;
import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PokerCardFactory {

	@Inject
	public PokerCardFactory() {
	}

	public Collection<PokerCardIdentifier> getCards() {
		final List<PokerCardIdentifier> result = new ArrayList<>();
		for (final PokerCardColor color : PokerCardColor.values()) {
			for (final PokerCardValue value : PokerCardValue.values()) {
				result.add(new PokerCardIdentifier(color, value));
			}
		}
		return result;
	}
}
