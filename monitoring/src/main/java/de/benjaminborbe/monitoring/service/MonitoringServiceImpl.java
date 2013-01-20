package de.benjaminborbe.monitoring.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.check.NodeCheckerCache;
import de.benjaminborbe.monitoring.check.RootNode;
import de.benjaminborbe.monitoring.check.SilentNodeRegistry;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.util.MonitoringMailer;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class MonitoringServiceImpl implements MonitoringService {

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final Provider<RootNode> rootNodeProvider;

	private final NodeCheckerCache nodeChecker;

	private final SilentNodeRegistry silentNodeRegistry;

	private final AuthorizationService authorizationService;

	private final MonitoringMailer monitoringMailer;

	private final DurationUtil durationUtil;

	private final MonitoringNodeDao monitoringNodeDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	@Inject
	public MonitoringServiceImpl(
			final Logger logger,
			final ValidationExecutor validationExecutor,
			final IdGeneratorUUID idGeneratorUUID,
			final Provider<RootNode> rootNodeProvider,
			final NodeCheckerCache nodeChecker,
			final SilentNodeRegistry silentNodeRegistry,
			final MonitoringMailer monitoringMailer,
			final AuthorizationService authorizationService,
			final DurationUtil durationUtil,
			final MonitoringNodeDao monitoringNodeDao) {
		this.logger = logger;
		this.validationExecutor = validationExecutor;
		this.idGeneratorUUID = idGeneratorUUID;
		this.rootNodeProvider = rootNodeProvider;
		this.nodeChecker = nodeChecker;
		this.silentNodeRegistry = silentNodeRegistry;
		this.monitoringMailer = monitoringMailer;
		this.authorizationService = authorizationService;
		this.durationUtil = durationUtil;
		this.monitoringNodeDao = monitoringNodeDao;
	}

	@Override
	public Collection<MonitoringCheckResult> checkRootNode(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.checkRootNode"));
			logger.trace("checkRootNode");
			return nodeChecker.checkNode(rootNodeProvider.get());
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<MonitoringCheckResult> checkRootNodeWithCache(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.checkRootNodeWithCache"));
			logger.trace("checkRootNodeWithCache");
			return nodeChecker.checkNodeWithCache(rootNodeProvider.get());
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void silentCheck(final SessionIdentifier sessionIdentifier, final String checkName) throws MonitoringServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.silentCheck"));
			logger.trace("silentCheck");
			silentNodeRegistry.add(checkName);
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void sendmail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, new PermissionIdentifier("MonitoringService.sendmail"));
			monitoringMailer.run();
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MonitoringNodeIdentifier createNodeIdentifier(final String id) throws MonitoringServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (id != null) {
				return new MonitoringNodeIdentifier(id);
			}
			else {
				return null;
			}
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			monitoringNodeDao.delete(monitoringNodeIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			final MonitoringNodeBean monitoringNode = monitoringNodeDao.load(node.getId());
			monitoringNode.setName(node.getName());
			monitoringNode.setCheckType(node.getCheckType());

			final ValidationResult errors = validationExecutor.validate(monitoringNode);
			if (errors.hasErrors()) {
				logger.warn("MonitoringNodeBean " + errors.toString());
				throw new ValidationException(errors);
			}
			monitoringNodeDao.save(monitoringNode);
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MonitoringNodeIdentifier createNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			final MonitoringNodeIdentifier id = createNodeIdentifier(idGeneratorUUID.nextId());
			final MonitoringNodeBean monitoringNode = monitoringNodeDao.create();
			monitoringNode.setId(id);
			monitoringNode.setName(node.getName());
			monitoringNode.setCheckType(node.getCheckType());

			final ValidationResult errors = validationExecutor.validate(monitoringNode);
			if (errors.hasErrors()) {
				logger.warn("MonitoringNodeBean " + errors.toString());
				throw new ValidationException(errors);
			}
			monitoringNodeDao.save(monitoringNode);

			return id;
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<MonitoringNode> listNodes(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			final List<MonitoringNode> result = new ArrayList<MonitoringNode>();
			final EntityIterator<MonitoringNodeBean> ni = monitoringNodeDao.getEntityIterator();
			while (ni.hasNext()) {
				result.add(ni.next());
			}
			return result;
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MonitoringNode getNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			return monitoringNodeDao.load(monitoringNodeIdentifier);
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

}
