package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PokerCardsStraightComparator implements Comparator<Collection<PokerCardIdentifier>> {

	private final PokerCardUtil pokerCardUtil;

	private final PokerValueComparator pokerValueComparator;

	@Inject
	public PokerCardsStraightComparator(final PokerCardUtil pokerCardUtil, final PokerValueComparator pokerValueComparator) {
		this.pokerCardUtil = pokerCardUtil;
		this.pokerValueComparator = pokerValueComparator;
	}

	@Override
	public int compare(final Collection<PokerCardIdentifier> cardsA, final Collection<PokerCardIdentifier> cardsB) {
		final List<PokerCardValue> listA = getStraight(cardsA);
		final List<PokerCardValue> listB = getStraight(cardsB);
		if (listA == null && listB == null) {
			return 0;
		} else if (listA != null && listB == null) {
			return 1;
		} else if (listA == null && listB != null) {
			return -1;
		} else {
			return pokerValueComparator.compare(listA.get(listA.size() - 1), listB.get(listB.size() - 1));
		}
	}

	private List<PokerCardValue> getStraight(final Collection<PokerCardIdentifier> cards) {
		final List<PokerCardValue> list = pokerCardUtil.sort(pokerCardUtil.unique(pokerCardUtil.values(cards)));

		for (int start = list.size() - 1; start >= 4; --start) {
			final List<PokerCardValue> result = new ArrayList<PokerCardValue>();
			result.add(list.get(start));
			for (int i = start - 1; i >= 0; --i) {
				if (list.get(i).getValue() == list.get(i + 1).getValue() - 1) {
					result.add(list.get(i));
				}
				if (result.size() == 5) {
					return result;
				}
			}
		}

		return null;
	}
}
