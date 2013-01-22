package de.benjaminborbe.monitoring.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MonitoringService {

	void mail(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	void check(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<MonitoringNode> getCheckResults(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	MonitoringNodeIdentifier createNodeIdentifier(String id) throws MonitoringServiceException;

	void deleteNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

	void updateNode(SessionIdentifier sessionIdentifier, MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException,
			ValidationException;

	MonitoringNodeIdentifier createNode(SessionIdentifier sessionIdentifier, MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException;

	Collection<MonitoringNode> listNodes(SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	MonitoringNode getNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

	Collection<String> getRequireParameter(SessionIdentifier sessionIdentifier, MonitoringCheckType monitoringCheckType) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

	void silentNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

}
