package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PokerValueComparator extends ComparatorBase<PokerCardValue, Integer> {

	@Override
	public Integer getValue(final PokerCardValue o) {
		return o != null ? new Integer(o.getValue()) : null;
	}

}
