package de.benjaminborbe.monitoring.mock;

import java.util.Collection;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;

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

	@Override
	public void sendmail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
	}

}
