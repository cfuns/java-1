package de.benjaminborbe.poker.util;

import com.google.inject.Inject;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PokerWinnerCalculator {

	private final PokerCardsComparator pokerCardsComparator;

	@Inject
	public PokerWinnerCalculator(final PokerCardsComparator pokerCardsComparator) {
		this.pokerCardsComparator = pokerCardsComparator;
	}

	public Collection<PokerPlayerIdentifier> getWinners(final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards) {
		final List<PokerPlayerIdentifier> result = new ArrayList<>();
		for (final Entry<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> e : playerCards.entrySet()) {
			if (result.isEmpty()) {
				result.add(e.getKey());
			} else {
				final Collection<PokerCardIdentifier> winnerCards = playerCards.get(result.get(0));
				final int c = pokerCardsComparator.compare(winnerCards, e.getValue());
				if (c == 0) {
					result.add(e.getKey());
				} else if (c > 0) {
					result.clear();
					result.add(e.getKey());
				}
			}
		}
		return result;
	}
}
