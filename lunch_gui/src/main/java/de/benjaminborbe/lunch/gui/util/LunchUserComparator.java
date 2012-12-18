package de.benjaminborbe.lunch.gui.util;

import de.benjaminborbe.lunch.api.LunchUser;
import de.benjaminborbe.tools.util.ComparatorBase;

public class LunchUserComparator extends ComparatorBase<LunchUser, String> {

	@Override
	public String getValue(final LunchUser o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
