package de.benjaminborbe.dashboard.gui.service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;

public class DashboardGuiWidgetTest {

	@Test
	public void testSortWidgets() {
		final DashboardGuiWidgetImpl dashboardWidget = new DashboardGuiWidgetImpl(null, null, null, null);
		final Set<DashboardContentWidget> dashboardContentWidgets = new HashSet<DashboardContentWidget>();
		dashboardContentWidgets.add(buildDashboardWidgetWithPrio(1337));
		dashboardContentWidgets.add(buildDashboardWidgetWithPrio(42));
		dashboardContentWidgets.add(buildDashboardWidgetWithPrio(23));
		dashboardContentWidgets.add(buildDashboardWidgetWithPrio(1));
		final List<DashboardContentWidget> result = dashboardWidget.sortWidgets(dashboardContentWidgets);
		assertEquals(1337, result.get(0).getPriority());
		assertEquals(42, result.get(1).getPriority());
		assertEquals(23, result.get(2).getPriority());
		assertEquals(1, result.get(3).getPriority());
	}

	private DashboardContentWidget buildDashboardWidgetWithPrio(final long prio) {
		final DashboardContentWidget d = EasyMock.createMock(DashboardContentWidget.class);
		EasyMock.expect(d.getPriority()).andReturn(prio).anyTimes();
		EasyMock.replay(d);
		return d;
	}
}
