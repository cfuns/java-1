package de.benjaminborbe.poker.gui.util;

import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PokerGameComparator extends ComparatorBase<PokerGame, String> {

	@Override
	public String getValue(final PokerGame o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
