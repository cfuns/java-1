package de.benjaminborbe.monitoring.mock;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;

import java.util.Collection;

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
	public MonitoringNodeIdentifier createNode(
		final SessionIdentifier sessionIdentifier,
		final MonitoringNodeDto node
	) throws MonitoringServiceException, LoginRequiredException,
		PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public Collection<MonitoringNode> listNodes(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public MonitoringNode getNode(
		final SessionIdentifier sessionIdentifier,
		final MonitoringNodeIdentifier monitoringNodeIdentifier
	) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public Collection<MonitoringNode> getCheckResults(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public void mail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public void check(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public void silentNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public void expectMonitoringAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
	}

	@Override
	public void expectMonitoringViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
	}

	@Override
	public void expectMonitoringViewPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
	}

	@Override
	public boolean hasMonitoringAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		return false;
	}

	@Override
	public boolean hasMonitoringViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		return false;
	}

	@Override
	public boolean hasMonitoringViewPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		return false;
	}

	@Override
	public Collection<String> getRequireParameter(final SessionIdentifier sessionIdentifier, final MonitoringCheckIdentifier monitoringCheckIdentifier)
		throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public Collection<MonitoringCheck> getMonitoringCheckTypes() throws MonitoringServiceException {
		return null;
	}

	@Override
	public MonitoringCheck getMonitoringCheckTypeById(final MonitoringCheckIdentifier monitoringCheckIdentifier) throws MonitoringServiceException {
		return null;
	}

	@Override
	public MonitoringCheckIdentifier getMonitoringCheckTypeDefault() throws MonitoringServiceException {
		return null;
	}

	@Override
	public void expectAuthToken(final String token) throws MonitoringServiceException, PermissionDeniedException {
	}

	@Override
	public Collection<MonitoringNode> getCheckResults(final String token) throws MonitoringServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public MonitoringNode getNode(
		final String token,
		final MonitoringNodeIdentifier monitoringNodeIdentifier
	) throws MonitoringServiceException, LoginRequiredException,
		PermissionDeniedException {
		return null;
	}

	@Override
	public void checkNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public void resetNodes(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public void unsilentNode(
		final SessionIdentifier sessionIdentifier,
		final MonitoringNodeIdentifier monitoringNodeIdentifier
	) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
	}

}
