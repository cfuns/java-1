package de.benjaminborbe.performance.util;

import de.benjaminborbe.performance.api.PerformanceService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;

public class PerformanceTrackerUnitTest {

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
