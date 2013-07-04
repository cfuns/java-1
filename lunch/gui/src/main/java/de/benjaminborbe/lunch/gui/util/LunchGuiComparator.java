package de.benjaminborbe.lunch.gui.util;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.tools.util.ComparatorBase;

import java.util.Date;

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
