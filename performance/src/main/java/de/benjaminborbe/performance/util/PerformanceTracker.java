package de.benjaminborbe.performance.util;

import java.util.List;

public interface PerformanceTracker {

	void track(String url, long duration);

	List<PerformanceEntry> getSlowestEntries(int limit);
}
