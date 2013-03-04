package de.benjaminborbe.checklist.gui.util;

import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.tools.util.ComparatorBase;

public class ChecklistGuiEntryComparator extends ComparatorBase<ChecklistEntry, String> {

	@Override
	public String getValue(final ChecklistEntry o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
