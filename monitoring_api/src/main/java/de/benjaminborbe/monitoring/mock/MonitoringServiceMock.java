package de.benjaminborbe.monitoring.mock;

import java.util.Collection;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;

public class MonitoringServiceMock implements MonitoringService {

	@Override
	public Collection<CheckResult> checkRootNode(final SessionIdentifier sessionIdentifier) {
		return null;
	}

	@Override
	public Collection<CheckResult> checkRootNodeWithCache(final SessionIdentifier sessionIdentifier) {
		return null;
	}

	@Override
	public void silentCheck(final SessionIdentifier sessionIdentifier, final String checkName) {
	}

}
