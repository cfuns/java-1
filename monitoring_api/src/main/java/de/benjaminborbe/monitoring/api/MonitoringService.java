package de.benjaminborbe.monitoring.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MonitoringService {

	Collection<CheckResult> checkRootNode(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException;

	Collection<CheckResult> checkRootNodeWithCache(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException;

	void silentCheck(final SessionIdentifier sessionIdentifier, String checkName) throws MonitoringServiceException, PermissionDeniedException;
}
