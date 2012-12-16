package de.benjaminborbe.checklist.gui.util;

import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.tools.util.ComparatorBase;

public class ChecklistListComparator extends ComparatorBase<ChecklistList, String> {

	@Override
	public String getValue(final ChecklistList o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
