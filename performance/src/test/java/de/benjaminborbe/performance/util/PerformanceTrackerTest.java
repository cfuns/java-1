package de.benjaminborbe.performance.util;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.performance.api.PerformanceService;

public class PerformanceTrackerTest {

	@Test
	public void testSlowest() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final PerformanceService performanceTracker = new PerformanceServiceImpl(logger);

		performanceTracker.track("/a", 1l);
		performanceTracker.track("/b", 2l);

		assertEquals(1, performanceTracker.getSlowestEntries(1).size());
		assertEquals(2, performanceTracker.getSlowestEntries(2).size());
		assertEquals(2, performanceTracker.getSlowestEntries(3).size());
	}
}
