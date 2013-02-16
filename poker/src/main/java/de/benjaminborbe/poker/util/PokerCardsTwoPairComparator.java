package de.benjaminborbe.poker.util;

import java.util.Collection;
import java.util.Comparator;
import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardIdentifier;

public class PokerCardsTwoPairComparator implements Comparator<Collection<PokerCardIdentifier>> {

	@Inject
	public PokerCardsTwoPairComparator() {
	}

	@Override
	public int compare(final Collection<PokerCardIdentifier> cardsA, final Collection<PokerCardIdentifier> cardsB) {
		return 0;
	}
}
