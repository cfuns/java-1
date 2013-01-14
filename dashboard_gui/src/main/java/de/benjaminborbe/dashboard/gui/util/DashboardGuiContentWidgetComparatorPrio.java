package de.benjaminborbe.dashboard.gui.util;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.util.ComparatorBase;

public class DashboardGuiContentWidgetComparatorPrio extends ComparatorBase<DashboardContentWidget, Long> {

	@Override
	public Long getValue(final DashboardContentWidget o) {
		return new Long(o.getPriority());
	}

	@Override
	public boolean inverted() {
		return true;
	}

}
