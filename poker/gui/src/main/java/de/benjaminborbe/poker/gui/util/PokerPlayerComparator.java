package de.benjaminborbe.poker.gui.util;

import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PokerPlayerComparator extends ComparatorBase<PokerPlayer, String> {

	@Override
	public String getValue(final PokerPlayer o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
