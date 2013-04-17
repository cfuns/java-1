package de.benjaminborbe.monitoring.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.check.MonitoringCheckNop;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBeanMapper;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.util.MonitoringChecker;
import de.benjaminborbe.monitoring.util.MonitoringNodeBuilder;
import de.benjaminborbe.monitoring.util.MonitoringNotifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public class MonitoringServiceImpl implements MonitoringService {

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final DurationUtil durationUtil;

	private final MonitoringNodeDao monitoringNodeDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final MonitoringCheckRegistry monitoringCheckRegistry;

	private final MonitoringNotifier monitoringMailer;

	private final MonitoringChecker monitoringChecker;

	private final MonitoringNodeBuilder monitoringNodeBuilder;

	private final MonitoringConfig monitoringConfig;

	@Inject
	public MonitoringServiceImpl(
		final Logger logger,
		final MonitoringCheckRegistry monitoringCheckRegistry,
		final MonitoringNodeBuilder monitoringNodeBuilder,
		final ValidationExecutor validationExecutor,
		final IdGeneratorUUID idGeneratorUUID,
		final AuthorizationService authorizationService,
		final DurationUtil durationUtil,
		final MonitoringNodeDao monitoringNodeDao,
		final MonitoringNotifier monitoringMailer,
		final MonitoringChecker monitoringChecker,
		final MonitoringConfig monitoringConfig) {
		this.logger = logger;
		this.monitoringCheckRegistry = monitoringCheckRegistry;
		this.monitoringNodeBuilder = monitoringNodeBuilder;
		this.validationExecutor = validationExecutor;
		this.idGeneratorUUID = idGeneratorUUID;
		this.authorizationService = authorizationService;
		this.durationUtil = durationUtil;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringMailer = monitoringMailer;
		this.monitoringChecker = monitoringChecker;
		this.monitoringConfig = monitoringConfig;
	}

	@Override
	public MonitoringNodeIdentifier createNodeIdentifier(final String id) throws MonitoringServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (id != null && !id.isEmpty()) {
				return new MonitoringNodeIdentifier(id);
			} else {
				return null;
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("deleteNode");
			monitoringNodeDao.delete(monitoringNodeIdentifier);
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
		PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("updateNode");

			final MonitoringNodeBean monitoringNode = monitoringNodeDao.load(node.getId());
			monitoringNode.setName(node.getName());
			monitoringNode.setCheckType(node.getCheckType());
			monitoringNode.setParameter(node.getParameter());
			monitoringNode.setActive(node.getActive());
			monitoringNode.setSilent(node.getSilent());
			monitoringNode.setParentId(node.getParentId());

			final ValidationResult errors = validationExecutor.validate(monitoringNode);
			if (errors.hasErrors()) {
				logger.warn("MonitoringNodeBean " + errors.toString());
				throw new ValidationException(errors);
			}
			monitoringNodeDao.save(monitoringNode);
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MonitoringNodeIdentifier createNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeDto node) throws MonitoringServiceException, LoginRequiredException,
		PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("createNode");

			final MonitoringNodeIdentifier id = createNodeIdentifier(idGeneratorUUID.nextId());
			final MonitoringNodeBean monitoringNode = monitoringNodeDao.create();
			monitoringNode.setId(id);
			monitoringNode.setName(node.getName());
			monitoringNode.setCheckType(node.getCheckType());
			monitoringNode.setParameter(node.getParameter());
			monitoringNode.setActive(node.getActive());
			monitoringNode.setSilent(node.getSilent());
			monitoringNode.setParentId(node.getParentId());

			final ValidationResult errors = validationExecutor.validate(monitoringNode);
			if (errors.hasErrors()) {
				logger.warn("MonitoringNodeBean " + errors.toString());
				throw new ValidationException(errors);
			}
			monitoringNodeDao.save(monitoringNode);

			return id;
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<MonitoringNode> listNodes(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("listNodes");

			final List<MonitoringNode> result = new ArrayList<>();
			final EntityIterator<MonitoringNodeBean> ni = monitoringNodeDao.getEntityIterator();
			while (ni.hasNext()) {
				result.add(monitoringNodeBuilder.build(ni.next()));
			}
			return result;
		} catch (final EntityIteratorException | StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MonitoringNode getNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("getNode");

			return monitoringNodeBuilder.build(monitoringNodeDao.load(monitoringNodeIdentifier));
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<String> getRequireParameter(final SessionIdentifier sessionIdentifier, final MonitoringCheckIdentifier monitoringCheckType) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("getRequireParameter");

			final MonitoringCheck check = monitoringCheckRegistry.get(monitoringCheckType);
			return check.getRequireParameters();
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<MonitoringNode> getCheckResults(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringViewOrAdminPermission(sessionIdentifier);
			logger.debug("getCheckResults");

			final List<MonitoringNode> result = new ArrayList<>();
			final EntityIterator<MonitoringNodeBean> ni = monitoringNodeDao.getEntityIterator();
			while (ni.hasNext()) {
				final MonitoringNodeBean node = ni.next();
				if (Boolean.TRUE.equals(node.getActive())) {
					result.add(monitoringNodeBuilder.build(node));
				}
			}
			return result;
		} catch (final StorageException | EntityIteratorException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void mail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("mail");

			monitoringMailer.mail();
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void check(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("check");

			monitoringChecker.checkAll();
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void silentNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("silentNode");

			final MonitoringNodeBean node = monitoringNodeDao.load(monitoringNodeIdentifier);
			node.setSilent(true);

			monitoringNodeDao.save(node, Arrays.asList(monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.SILENT)));
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectMonitoringAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(MONITORING_ROLE_ADMIN));
		} catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public void expectMonitoringViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
		try {
			authorizationService.expectOneOfPermissions(sessionIdentifier, authorizationService.createPermissionIdentifier(MONITORING_ROLE_ADMIN),
				authorizationService.createPermissionIdentifier(MONITORING_ROLE_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public void expectMonitoringViewPermission(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(MONITORING_ROLE_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public boolean hasMonitoringAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(MONITORING_ROLE_ADMIN));
		} catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public boolean hasMonitoringViewOrAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		try {
			return authorizationService.hasOneOfPermissions(sessionIdentifier, authorizationService.createPermissionIdentifier(MONITORING_ROLE_ADMIN),
				authorizationService.createPermissionIdentifier(MONITORING_ROLE_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public boolean hasMonitoringViewPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(MONITORING_ROLE_VIEW));
		} catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public Collection<MonitoringCheck> getMonitoringCheckTypes() throws MonitoringServiceException {
		final List<MonitoringCheck> result = new ArrayList<>();
		for (final MonitoringCheck type : monitoringCheckRegistry.getAll()) {
			result.add(type);
		}
		return result;
	}

	@Override
	public MonitoringCheck getMonitoringCheckTypeById(final MonitoringCheckIdentifier monitoringNodeIdentifier) throws MonitoringServiceException {
		return monitoringCheckRegistry.get(monitoringNodeIdentifier);
	}

	@Override
	public MonitoringCheckIdentifier getMonitoringCheckTypeDefault() throws MonitoringServiceException {
		return new MonitoringCheckIdentifier(MonitoringCheckNop.ID);
	}

	@Override
	public void expectAuthToken(final String token) throws MonitoringServiceException, PermissionDeniedException {
		if (monitoringConfig.getAuthToken() == null || token == null || !monitoringConfig.getAuthToken().equals(token)) {
			throw new PermissionDeniedException("invalid token");
		}
	}

	@Override
	public Collection<MonitoringNode> getCheckResults(final String token) throws MonitoringServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAuthToken(token);
			logger.debug("getCheckResults");

			final List<MonitoringNode> result = new ArrayList<>();
			final EntityIterator<MonitoringNodeBean> ni = monitoringNodeDao.getEntityIterator();
			while (ni.hasNext()) {
				final MonitoringNodeBean node = ni.next();
				if (Boolean.TRUE.equals(node.getActive())) {
					result.add(monitoringNodeBuilder.build(node));
				}
			}
			return result;
		} catch (final StorageException | EntityIteratorException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public MonitoringNode getNode(final String token, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException, LoginRequiredException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAuthToken(token);
			logger.debug("getNode");

			return monitoringNodeBuilder.build(monitoringNodeDao.load(monitoringNodeIdentifier));
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void checkNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("checkNode");

			final MonitoringNodeBean node = monitoringNodeDao.load(monitoringNodeIdentifier);
			monitoringChecker.check(node);
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void unsilentNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
		LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminPermission(sessionIdentifier);
			logger.debug("unsilentNode");

			final MonitoringNodeBean node = monitoringNodeDao.load(monitoringNodeIdentifier);
			node.setSilent(false);

			monitoringNodeDao.save(node, Arrays.asList(monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.SILENT)));
		} catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
