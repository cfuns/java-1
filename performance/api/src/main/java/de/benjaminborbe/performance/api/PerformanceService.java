package de.benjaminborbe.performance.api;

import java.util.List;

public interface PerformanceService {

	void track(String url, long duration);

	List<PerformanceEntry> getSlowestEntries(int limit);
}
