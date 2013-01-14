package de.benjaminborbe.dashboard.gui.util;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.util.ComparatorBase;

public class DashboardContentWidgetComparatorTitle extends ComparatorBase<DashboardContentWidget, String> {

	@Override
	public String getValue(final DashboardContentWidget o) {
		return o.getTitle() != null ? o.getTitle().toLowerCase() : null;
	}

}
