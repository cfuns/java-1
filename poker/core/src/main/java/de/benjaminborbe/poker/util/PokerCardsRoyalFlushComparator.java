package de.benjaminborbe.poker.util;

import javax.inject.Inject;
import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.map.MapList;
import de.benjaminborbe.tools.util.ComparatorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PokerCardsRoyalFlushComparator implements Comparator<Collection<PokerCardIdentifier>> {

	private final PokerCardComparator pokerCardComparator;

	private final PokerCardUtil pokerCardUtil;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public PokerCardsRoyalFlushComparator(final ComparatorUtil comparatorUtil, final PokerCardUtil pokerCardUtil, final PokerCardComparator pokerCardComparator) {
		this.comparatorUtil = comparatorUtil;
		this.pokerCardUtil = pokerCardUtil;
		this.pokerCardComparator = pokerCardComparator;
	}

	@Override
	public int compare(final Collection<PokerCardIdentifier> cardsA, final Collection<PokerCardIdentifier> cardsB) {
		final List<PokerCardIdentifier> listA = getStraightFlush(cardsA);
		final List<PokerCardIdentifier> listB = getStraightFlush(cardsB);
		if (listA == null && listB == null) {
			return 0;
		} else if (listA != null && listB == null) {
			return 1;
		} else if (listA == null && listB != null) {
			return -1;
		} else {
			return pokerCardComparator.compare(listA.get(listA.size() - 1), listB.get(listB.size() - 1));
		}
	}

	private List<PokerCardIdentifier> getStraightFlush(final Collection<PokerCardIdentifier> cards) {
		final MapList<PokerCardColor, PokerCardIdentifier> map = pokerCardUtil.groupByColor(cards);
		for (final List<PokerCardIdentifier> v : map.values()) {
			final List<PokerCardIdentifier> values = comparatorUtil.sort(v, pokerCardComparator);
			final List<PokerCardIdentifier> result = new ArrayList<>();
			if (values.size() >= 5) {
				final PokerCardIdentifier first = values.get(values.size() - 1);
				if (PokerCardValue.ACE.equals(first.getValue())) {
					result.add(first);
					for (int i = values.size() - 2; i >= 0; --i) {
						if (values.get(i).getValue().getValue() == values.get(i + 1).getValue().getValue() - 1) {
							result.add(values.get(i));
						}
						if (result.size() == 5) {
							return result;
						}
					}
				}
			}
		}
		return null;
	}
}
