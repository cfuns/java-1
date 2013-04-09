package de.benjaminborbe.cron.gui.util;

import de.benjaminborbe.cron.api.CronIdentifier;
import de.benjaminborbe.tools.util.ComparatorBase;

public class CronComparator extends ComparatorBase<CronIdentifier, String> {

	@Override
	public String getValue(final CronIdentifier o) {
		return o.getId() != null ? o.getId().toLowerCase() : null;
	}

}
