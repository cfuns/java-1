package de.benjaminborbe.poker.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.tools.map.MapList;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class PokerCardsFlushComparator implements Comparator<Collection<PokerCardIdentifier>> {

	private final PokerCardComparator pokerCardComparator;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public PokerCardsFlushComparator(final ComparatorUtil comparatorUtil, final PokerCardComparator pokerCardComparator) {
		this.comparatorUtil = comparatorUtil;
		this.pokerCardComparator = pokerCardComparator;
	}

	@Override
	public int compare(final Collection<PokerCardIdentifier> cardsA, final Collection<PokerCardIdentifier> cardsB) {
		final List<PokerCardIdentifier> listA = buildList(cardsA);
		final List<PokerCardIdentifier> listB = buildList(cardsB);
		if (null == null && listB == null) {
			return 0;
		}
		else if (listA != null && listB == null) {
			return 1;
		}
		else if (listA == null && listB != null) {
			return -1;
		}
		for (int i = 1; i <= 5; ++i) {
			final int result = pokerCardComparator.compare(listA.get(listA.size() - i), listB.get(listB.size() - i));
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	private List<PokerCardIdentifier> buildList(final Collection<PokerCardIdentifier> cards) {
		final MapList<PokerCardColor, PokerCardIdentifier> map = buildMap(cards);
		for (final List<PokerCardIdentifier> e : map.values()) {
			if (e.size() >= 5) {
				return comparatorUtil.sort(e, pokerCardComparator);
			}
		}
		return null;
	}

	private MapList<PokerCardColor, PokerCardIdentifier> buildMap(final Collection<PokerCardIdentifier> cards) {
		final MapList<PokerCardColor, PokerCardIdentifier> result = new MapList<PokerCardColor, PokerCardIdentifier>();
		for (final PokerCardIdentifier card : cards) {
			result.add(card.getColor(), card);
		}
		return result;
	}
}
