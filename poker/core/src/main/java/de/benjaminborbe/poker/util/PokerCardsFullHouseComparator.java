package de.benjaminborbe.poker.util;

import javax.inject.Inject;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.map.MapList;
import de.benjaminborbe.tools.util.ComparatorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public class PokerCardsFullHouseComparator implements Comparator<Collection<PokerCardIdentifier>> {

	private final PokerValueComparator pokerValueComparator;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public PokerCardsFullHouseComparator(final ComparatorUtil comparatorUtil, final PokerValueComparator pokerValueComparator) {
		this.comparatorUtil = comparatorUtil;
		this.pokerValueComparator = pokerValueComparator;
	}

	@Override
	public int compare(final Collection<PokerCardIdentifier> cardsA, final Collection<PokerCardIdentifier> cardsB) {
		final List<PokerCardValue> listA3 = buildList(cardsA, 3);
		final List<PokerCardValue> listA2 = buildList(cardsA, 2);
		final List<PokerCardValue> listB3 = buildList(cardsB, 3);
		final List<PokerCardValue> listB2 = buildList(cardsB, 2);
		if (listA3.size() >= 1 && listA2.size() >= 2 && listB3.size() >= 1 && listB2.size() >= 2) {
			final PokerCardValue a3 = listA3.get(listA3.size() - 1);
			final PokerCardValue b3 = listB3.get(listB3.size() - 1);
			final int valueResult = pokerValueComparator.compare(a3, b3);
			if (valueResult != 0) {
				return valueResult;
			}
			listA2.remove(a3);
			listB2.remove(b3);
			return pokerValueComparator.compare(listA2.get(listA2.size() - 1), listB2.get(listB2.size() - 1));
		}
		if (listA3.size() >= 1 && listA2.size() >= 2) {
			return 1;
		}
		if (listB3.size() >= 1 && listB2.size() >= 2) {
			return -1;
		}
		return 0;
	}

	private List<PokerCardValue> buildList(final Collection<PokerCardIdentifier> cards, final int length) {
		final List<PokerCardValue> list = new ArrayList<>();
		final MapList<PokerCardValue, PokerCardIdentifier> map = buildMap(cards);
		for (final Entry<PokerCardValue, List<PokerCardIdentifier>> e : map.entrySet()) {
			if (e.getValue().size() >= length) {
				list.add(e.getValue().get(0).getValue());
			}
		}
		return comparatorUtil.sort(list, pokerValueComparator);
	}

	private MapList<PokerCardValue, PokerCardIdentifier> buildMap(final Collection<PokerCardIdentifier> cards) {
		final MapList<PokerCardValue, PokerCardIdentifier> result = new MapList<>();
		for (final PokerCardIdentifier card : cards) {
			result.add(card.getValue(), card);
		}
		return result;
	}
}
