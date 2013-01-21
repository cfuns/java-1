package de.benjaminborbe.monitoring.gui.util;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.tools.util.ComparatorBase;

public class MonitoringNodeComparator extends ComparatorBase<MonitoringNode, String> {

	@Override
	public String getValue(final MonitoringNode o) {
		return o.getDescription() != null ? o.getDescription().toLowerCase() : null;
	}
}
