package de.benjaminborbe.dashboard.gui.util;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class DashboardGuiContentWidgetComparator extends ComparatorChain<DashboardContentWidget> {

	@SuppressWarnings("unchecked")
	@Inject
	public DashboardGuiContentWidgetComparator(final DashboardGuiContentWidgetComparatorPrio prio, final DashboardGuiContentWidgetComparatorTitle title) {
		super(prio, title);
	}

}
