package de.benjaminborbe.poker.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class PokerCardsHighcardComparator implements Comparator<Collection<PokerCardIdentifier>> {

	private final ComparatorUtil comparatorUtil;

	private final PokerCardComparator pokerCardComparator;

	@Inject
	public PokerCardsHighcardComparator(final ComparatorUtil comparatorUtil, final PokerCardComparator pokerCardComparator) {
		this.comparatorUtil = comparatorUtil;
		this.pokerCardComparator = pokerCardComparator;
	}

	@Override
	public int compare(final Collection<PokerCardIdentifier> cardsA, final Collection<PokerCardIdentifier> cardsB) {
		final List<PokerCardIdentifier> listA = comparatorUtil.sort(cardsA, pokerCardComparator);
		final List<PokerCardIdentifier> listB = comparatorUtil.sort(cardsB, pokerCardComparator);
		if (listA.isEmpty() && listB.isEmpty()) {
			return 0;
		}
		if (listA.isEmpty()) {
			return -1;
		}
		if (listB.isEmpty()) {
			return 1;
		}
		final int result = pokerCardComparator.compare(listA.get(listA.size() - 1), listB.get(listB.size() - 1));
		if (result != 0) {
			return result;
		}
		if (listA.size() < 2 && listB.size() < 2) {
			return 0;
		}
		if (listA.size() < 2) {
			return -1;
		}
		if (listB.size() < 2) {
			return 1;
		}
		return pokerCardComparator.compare(listA.get(listA.size() - 2), listB.get(listB.size() - 2));
	}
}
