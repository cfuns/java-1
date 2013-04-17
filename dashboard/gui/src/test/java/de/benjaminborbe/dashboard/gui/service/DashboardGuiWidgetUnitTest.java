package de.benjaminborbe.dashboard.gui.service;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.dashboard.gui.util.DashboardGuiContentWidgetComparator;
import de.benjaminborbe.dashboard.gui.util.DashboardGuiContentWidgetComparatorPrio;
import de.benjaminborbe.dashboard.gui.util.DashboardGuiContentWidgetComparatorTitle;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DashboardGuiWidgetUnitTest {

	@Test
	public void testSortWidgets() {
		final List<DashboardContentWidget> result = new ArrayList<>();
		result.add(buildDashboardWidgetWithPrio(42, "a"));
		result.add(buildDashboardWidgetWithPrio(42, "b"));
		result.add(buildDashboardWidgetWithPrio(1, "a"));
		result.add(buildDashboardWidgetWithPrio(23, "b"));
		result.add(buildDashboardWidgetWithPrio(23, "a"));
		result.add(buildDashboardWidgetWithPrio(1337, "a"));

		final DashboardGuiContentWidgetComparator c = new DashboardGuiContentWidgetComparator(new DashboardGuiContentWidgetComparatorPrio(),
			new DashboardGuiContentWidgetComparatorTitle());
		Collections.sort(result, c);
		assertEquals(1337, result.get(0).getPriority());
		assertEquals("a", result.get(0).getTitle());
		assertEquals(42, result.get(1).getPriority());
		assertEquals("a", result.get(1).getTitle());
		assertEquals(42, result.get(2).getPriority());
		assertEquals("b", result.get(2).getTitle());
		assertEquals(23, result.get(3).getPriority());
		assertEquals("a", result.get(3).getTitle());
		assertEquals(23, result.get(4).getPriority());
		assertEquals("b", result.get(4).getTitle());
		assertEquals(1, result.get(5).getPriority());
		assertEquals("a", result.get(5).getTitle());
	}

	private DashboardContentWidget buildDashboardWidgetWithPrio(final long prio, final String title) {
		final DashboardContentWidget d = EasyMock.createMock(DashboardContentWidget.class);
		EasyMock.expect(d.getPriority()).andReturn(prio).anyTimes();
		EasyMock.expect(d.getTitle()).andReturn(title).anyTimes();
		EasyMock.replay(d);
		return d;
	}
}
