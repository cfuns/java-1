package de.benjaminborbe.poker.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.map.MapList;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class PokerCardUtil {

	private final ComparatorUtil comparatorUtil;

	private final PokerValueComparator pokerValueComparator;

	@Inject
	public PokerCardUtil(final ComparatorUtil comparatorUtil, final PokerValueComparator pokerValueComparator) {
		this.comparatorUtil = comparatorUtil;
		this.pokerValueComparator = pokerValueComparator;
	}

	public MapList<PokerCardColor, PokerCardIdentifier> groupByColor(final Collection<PokerCardIdentifier> cards) {
		final MapList<PokerCardColor, PokerCardIdentifier> result = new MapList<PokerCardColor, PokerCardIdentifier>();
		for (final PokerCardIdentifier card : cards) {
			result.add(card.getColor(), card);
		}
		return result;
	}

	public List<PokerCardValue> values(final Collection<PokerCardIdentifier> cards) {
		final List<PokerCardValue> result = new ArrayList<PokerCardValue>();
		for (final PokerCardIdentifier card : cards) {
			result.add(card.getValue());
		}
		return result;
	}

	public List<PokerCardColor> colors(final Collection<PokerCardIdentifier> cards) {
		final List<PokerCardColor> result = new ArrayList<PokerCardColor>();
		for (final PokerCardIdentifier card : cards) {
			result.add(card.getColor());
		}
		return result;
	}

	public List<PokerCardValue> unique(final List<PokerCardValue> values) {
		final List<PokerCardValue> result = new ArrayList<PokerCardValue>();
		for (final PokerCardValue value : values) {
			if (!result.contains(value)) {
				result.add(value);
			}
		}
		return result;
	}

	public List<PokerCardValue> sort(final Collection<PokerCardValue> list) {
		return comparatorUtil.sort(list, pokerValueComparator);
	}
}
