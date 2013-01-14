package de.benjaminborbe.dashboard.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.util.ComparatorChain;

public class DashboardContentWidgetComparator extends ComparatorChain<DashboardContentWidget> {

	@SuppressWarnings("unchecked")
	@Inject
	public DashboardContentWidgetComparator(final DashboardContentWidgetComparatorPrio prio, final DashboardContentWidgetComparatorTitle title) {
		super(prio, title);
	}

}
