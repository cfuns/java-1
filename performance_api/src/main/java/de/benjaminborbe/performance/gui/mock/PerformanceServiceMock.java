package de.benjaminborbe.performance.gui.mock;

import java.util.List;

import de.benjaminborbe.performance.api.PerformanceEntry;
import de.benjaminborbe.performance.api.PerformanceService;

public class PerformanceServiceMock implements PerformanceService {

	@Override
	public void track(final String url, final long duration) {
	}

	@Override
	public List<PerformanceEntry> getSlowestEntries(final int limit) {
		return null;
	}

}
