package de.benjaminborbe.lunch.gui.util;

import de.benjaminborbe.kiosk.api.KioskUser;
import de.benjaminborbe.tools.util.ComparatorBase;

public class KioskUserComparator extends ComparatorBase<KioskUser, String> {

	@Override
	public String getValue(final KioskUser o) {
		return o.getPrename() != null && o.getSurname() != null ? (o.getPrename() + " " + o.getSurname()).toLowerCase() : null;
	}

}
