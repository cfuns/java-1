package de.benjaminborbe.dashboard.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.util.ComparatorChain;

public class DashboardGuiContentWidgetComparator extends ComparatorChain<DashboardContentWidget> {

	@SuppressWarnings("unchecked")
	@Inject
	public DashboardGuiContentWidgetComparator(final DashboardGuiContentWidgetComparatorPrio prio, final DashboardGuiContentWidgetComparatorTitle title) {
		super(prio, title);
	}

}
