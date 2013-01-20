package de.benjaminborbe.monitoring.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MonitoringService {

	Collection<MonitoringCheckResult> checkRootNode(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<MonitoringCheckResult> checkRootNodeWithCache(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

	void silentCheck(final SessionIdentifier sessionIdentifier, String checkName) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	void sendmail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	MonitoringNodeIdentifier createNodeIdentifier(String id) throws MonitoringServiceException;

	void deleteNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;
}
