package de.benjaminborbe.monitoring.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface MonitoringService {

	String MONITORING_ROLE_ADMIN = "MonitoringAdmin";

	String MONITORING_ROLE_VIEW = "MonitoringView";

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

	MonitoringNode getNode(String token, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException;

	MonitoringNode getNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

	Collection<String> getRequireParameter(SessionIdentifier sessionIdentifier, MonitoringCheckIdentifier monitoringCheckIdentifier) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException;

	void silentNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

	void expectMonitoringAdminRole(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException;

	void expectMonitoringViewOrAdminRole(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException;

	void expectMonitoringViewRole(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException;

	boolean hasMonitoringAdminRole(SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException;

	boolean hasMonitoringViewOrAdminRole(SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException;

	boolean hasMonitoringViewRole(SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException;

	Collection<MonitoringCheck> getMonitoringCheckTypes() throws MonitoringServiceException;

	MonitoringCheckIdentifier getMonitoringCheckTypeDefault() throws MonitoringServiceException;

	MonitoringCheck getMonitoringCheckTypeById(MonitoringCheckIdentifier monitoringCheckIdentifier) throws MonitoringServiceException;

	void expectAuthToken(String token) throws MonitoringServiceException, PermissionDeniedException;

	Collection<MonitoringNode> getCheckResults(String token) throws MonitoringServiceException, PermissionDeniedException;

	void checkNode(SessionIdentifier sessionIdentifier, MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException;

}
