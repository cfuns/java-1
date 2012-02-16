package de.benjaminborbe.monitoring.api;

import java.util.Collection;

public interface MonitoringService {

	Collection<CheckResult> checkRootNode();

	Collection<CheckResult> checkRootNodeWithCache();

	void silentCheck(String checkName);
}
