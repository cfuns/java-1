package de.benjaminborbe.monitoring.tools;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.tools.util.ComparatorBase;

public class MonitoringNodeComparator<N extends MonitoringNode> extends ComparatorBase<N, String> {

	@Override
	public String getValue(final N o) {
		return o.getDescription() != null ? o.getDescription().toLowerCase() : null;
	}
}
