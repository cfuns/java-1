package de.benjaminborbe.monitoring.mock;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;

public class MonitoringServiceMock implements MonitoringService {

	@Override
	public MonitoringNodeIdentifier createNodeIdentifier(final String id) throws MonitoringServiceException {
		return null;
	}

	@Override
	public void deleteNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public void updateNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
	}

	@Override
	public MonitoringNodeIdentifier createNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public Collection<MonitoringNode> listNodes(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public MonitoringNode getNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public Collection<String> getRequireParameter(final SessionIdentifier sessionIdentifier, final MonitoringCheckType monitoringCheckType) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		return null;
	}

}
