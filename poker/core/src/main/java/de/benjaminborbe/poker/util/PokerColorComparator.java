package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PokerColorComparator extends ComparatorBase<PokerCardColor, Integer> {

	@Override
	public Integer getValue(final PokerCardColor o) {
		return o != null ? new Integer(o.getValue()) : null;
	}

}
