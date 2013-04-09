package de.benjaminborbe.monitoring.gui.util;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.tools.util.ComparatorBase;

public class MonitoringCheckComparator extends ComparatorBase<MonitoringCheck, String> {

	@Override
	public String getValue(final MonitoringCheck o) {
		return o.getTitle() != null ? o.getTitle().toLowerCase() : null;
	}

}
