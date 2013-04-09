package de.benjaminborbe.lunch.gui.util;

import java.util.Date;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.tools.util.ComparatorBase;

public class LunchGuiComparator extends ComparatorBase<Lunch, Date> {

	@Override
	public Date getValue(final Lunch o) {
		return o.getDate();
	}

	@Override
	public boolean inverted() {
		return true;
	}

}
