package de.benjaminborbe.monitoring.api;

import java.util.Collection;

public interface MonitoringService {

	Collection<CheckResult> checkRootNode();

	Collection<CheckResult> checkRootNodeWithCache();
}
