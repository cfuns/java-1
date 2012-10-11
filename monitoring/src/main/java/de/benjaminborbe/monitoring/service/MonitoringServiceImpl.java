package de.benjaminborbe.monitoring.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.check.NodeCheckerCache;
import de.benjaminborbe.monitoring.check.RootNode;
import de.benjaminborbe.monitoring.check.SilentNodeRegistry;
import de.benjaminborbe.monitoring.util.MonitoringMailer;

@Singleton
public class MonitoringServiceImpl implements MonitoringService {

	private final Logger logger;

	private final Provider<RootNode> rootNodeProvider;

	private final NodeCheckerCache nodeChecker;

	private final SilentNodeRegistry silentNodeRegistry;

	private final AuthorizationService authorizationService;

	private final MonitoringMailer monitoringMailer;

	@Inject
	public MonitoringServiceImpl(
			final Logger logger,
			final Provider<RootNode> rootNodeProvider,
			final NodeCheckerCache nodeChecker,
			final SilentNodeRegistry silentNodeRegistry,
			final MonitoringMailer monitoringMailer,
			final AuthorizationService authorizationService) {
		this.logger = logger;
		this.rootNodeProvider = rootNodeProvider;
		this.nodeChecker = nodeChecker;
		this.silentNodeRegistry = silentNodeRegistry;
		this.monitoringMailer = monitoringMailer;
		this.authorizationService = authorizationService;
	}

	@Override
	public Collection<CheckResult> checkRootNode(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.checkRootNode"));
			logger.trace("checkRootNode");
			return nodeChecker.checkNode(rootNodeProvider.get());
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException("AuthorizationServiceException", e);
		}
	}

	@Override
	public Collection<CheckResult> checkRootNodeWithCache(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.checkRootNodeWithCache"));
			logger.trace("checkRootNodeWithCache");
			return nodeChecker.checkNodeWithCache(rootNodeProvider.get());
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException("AuthorizationServiceException", e);
		}
	}

	@Override
	public void silentCheck(final SessionIdentifier sessionIdentifier, final String checkName) throws MonitoringServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.silentCheck"));
			logger.trace("silentCheck");
			silentNodeRegistry.add(checkName);
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException("AuthorizationServiceException", e);
		}
	}

	@Override
	public void sendmail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.sendmail"));
			monitoringMailer.run();
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException("AuthorizationServiceException", e);
		}
	}

}
