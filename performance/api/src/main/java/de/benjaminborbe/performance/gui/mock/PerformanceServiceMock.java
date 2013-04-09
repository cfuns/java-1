package de.benjaminborbe.performance.gui.mock;

import de.benjaminborbe.performance.api.PerformanceEntry;
import de.benjaminborbe.performance.api.PerformanceService;

import java.util.List;

public class PerformanceServiceMock implements PerformanceService {

	@Override
	public void track(final String url, final long duration) {
	}

	@Override
	public List<PerformanceEntry> getSlowestEntries(final int limit) {
		return null;
	}

}
