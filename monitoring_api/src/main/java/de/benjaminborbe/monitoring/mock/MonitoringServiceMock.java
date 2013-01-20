package de.benjaminborbe.monitoring.mock;

import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;

public class MonitoringServiceMock implements MonitoringService {

	@Override
	public Collection<MonitoringCheckResult> checkRootNode(final SessionIdentifier sessionIdentifier) {
		return null;
	}

	@Override
	public Collection<MonitoringCheckResult> checkRootNodeWithCache(final SessionIdentifier sessionIdentifier) {
		return null;
	}

	@Override
	public void silentCheck(final SessionIdentifier sessionIdentifier, final String checkName) {
	}

	@Override
	public void sendmail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException {
	}

	@Override
	public MonitoringNodeIdentifier createNodeIdentifier(final String id) throws MonitoringServiceException {
		return null;
	}

	@Override
	public void deleteNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException {
	}

}
