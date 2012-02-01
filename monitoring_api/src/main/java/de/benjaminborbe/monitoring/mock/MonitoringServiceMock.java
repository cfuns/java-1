package de.benjaminborbe.monitoring.mock;

import java.util.Collection;
import java.util.HashSet;

import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;

public class MonitoringServiceMock implements MonitoringService {

	@Override
	public Collection<CheckResult> checkRootNode() {
		return new HashSet<CheckResult>();
	}

	@Override
	public Collection<CheckResult> checkRootNodeWithCache() {
		return new HashSet<CheckResult>();
	}

}
