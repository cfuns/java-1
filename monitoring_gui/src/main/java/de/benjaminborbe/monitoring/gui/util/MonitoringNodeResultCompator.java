package de.benjaminborbe.monitoring.gui.util;

import de.benjaminborbe.monitoring.api.MonitoringNodeResult;
import de.benjaminborbe.tools.util.ComparatorBase;

public class MonitoringNodeResultCompator extends ComparatorBase<MonitoringNodeResult, String> {

	@Override
	public String getValue(final MonitoringNodeResult o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
