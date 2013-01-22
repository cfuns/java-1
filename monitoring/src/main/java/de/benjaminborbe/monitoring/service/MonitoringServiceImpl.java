package de.benjaminborbe.monitoring.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBeanMapper;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
import de.benjaminborbe.monitoring.util.MonitoringChecker;
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

	private final class MonitoringNodeDescription implements MonitoringNode {

		private final MonitoringNodeBean node;

		private MonitoringNodeDescription(final MonitoringNodeBean node) {
			this.node = node;
		}

		@Override
		public Boolean getResult() {
			return node.getResult();
		}

		@Override
		public String getMessage() {
			return node.getMessage();
		}

		@Override
		public String getName() {
			return node.getName();
		}

		@Override
		public MonitoringNodeIdentifier getId() {
			return node.getId();
		}

		@Override
		public String getDescription() {
			final MonitoringCheck check = monitoringCheckFactory.get(node.getCheckType());
			return check.getDescription(node.getParameter());
		}

		@Override
		public MonitoringCheckType getCheckType() {
			return node.getCheckType();
		}

		@Override
		public Map<String, String> getParameter() {
			return node.getParameter();
		}

		@Override
		public Boolean getSilent() {
			return node.getSilent();
		}

		@Override
		public Boolean getActive() {
			return node.getActive();
		}
	}

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final DurationUtil durationUtil;

	private final MonitoringNodeDao monitoringNodeDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final MonitoringCheckFactory monitoringCheckFactory;

	private final MonitoringMailer monitoringMailer;

	private final MonitoringChecker monitoringChecker;

	@Inject
	public MonitoringServiceImpl(
			final Logger logger,
			final MonitoringCheckFactory monitoringCheckFactory,
			final ValidationExecutor validationExecutor,
			final IdGeneratorUUID idGeneratorUUID,
			final AuthorizationService authorizationService,
			final DurationUtil durationUtil,
			final MonitoringNodeDao monitoringNodeDao,
			final MonitoringMailer monitoringMailer,
			final MonitoringChecker monitoringChecker) {
		this.logger = logger;
		this.monitoringCheckFactory = monitoringCheckFactory;
		this.validationExecutor = validationExecutor;
		this.idGeneratorUUID = idGeneratorUUID;
		this.authorizationService = authorizationService;
		this.durationUtil = durationUtil;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringMailer = monitoringMailer;
		this.monitoringChecker = monitoringChecker;
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
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("deleteNode");
			monitoringNodeDao.delete(monitoringNodeIdentifier);
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
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("updateNode");

			final MonitoringNodeBean monitoringNode = monitoringNodeDao.load(node.getId());
			monitoringNode.setName(node.getName());
			monitoringNode.setCheckType(node.getCheckType());
			monitoringNode.setParameter(node.getParameter());
			monitoringNode.setActive(node.getActive());
			monitoringNode.setSilent(node.getSilent());

			final ValidationResult errors = validationExecutor.validate(monitoringNode);
			if (errors.hasErrors()) {
				logger.warn("MonitoringNodeBean " + errors.toString());
				throw new ValidationException(errors);
			}
			monitoringNodeDao.save(monitoringNode);
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
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("createNode");

			final MonitoringNodeIdentifier id = createNodeIdentifier(idGeneratorUUID.nextId());
			final MonitoringNodeBean monitoringNode = monitoringNodeDao.create();
			monitoringNode.setId(id);
			monitoringNode.setName(node.getName());
			monitoringNode.setCheckType(node.getCheckType());
			monitoringNode.setParameter(node.getParameter());
			monitoringNode.setActive(node.getActive());
			monitoringNode.setSilent(node.getSilent());

			final ValidationResult errors = validationExecutor.validate(monitoringNode);
			if (errors.hasErrors()) {
				logger.warn("MonitoringNodeBean " + errors.toString());
				throw new ValidationException(errors);
			}
			monitoringNodeDao.save(monitoringNode);

			return id;
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
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("listNodes");

			final List<MonitoringNode> result = new ArrayList<MonitoringNode>();
			final EntityIterator<MonitoringNodeBean> ni = monitoringNodeDao.getEntityIterator();
			while (ni.hasNext()) {
				result.add(new MonitoringNodeDescription(ni.next()));
			}
			return result;
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
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("getNode");

			return new MonitoringNodeDescription(monitoringNodeDao.load(monitoringNodeIdentifier));
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
	public Collection<String> getRequireParameter(final SessionIdentifier sessionIdentifier, final MonitoringCheckType monitoringCheckType) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("getRequireParameter");

			final MonitoringCheck check = monitoringCheckFactory.get(monitoringCheckType);
			return check.getRequireParameters();
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<MonitoringNode> getCheckResults(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("getCheckResults");

			final List<MonitoringNode> result = new ArrayList<MonitoringNode>();
			final EntityIterator<MonitoringNodeBean> ni = monitoringNodeDao.getEntityIterator();
			while (ni.hasNext()) {
				final MonitoringNodeBean node = ni.next();
				if (Boolean.TRUE.equals(node.getActive())) {
					result.add(new MonitoringNodeDescription(node));
				}
			}
			return result;
		}
		catch (final StorageException e) {
			throw new MonitoringServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void mail(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("mail");

			monitoringMailer.mail();
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void check(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("check");

			monitoringChecker.check();
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void silentNode(final SessionIdentifier sessionIdentifier, final MonitoringNodeIdentifier monitoringNodeIdentifier) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectMonitoringAdminRole(sessionIdentifier);
			logger.debug("silentNode");

			final MonitoringNodeBean node = monitoringNodeDao.load(monitoringNodeIdentifier);
			node.setSilent(true);

			monitoringNodeDao.save(node, Arrays.asList(monitoringNodeDao.buildValue(MonitoringNodeBeanMapper.SILENT)));
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
	public void expectMonitoringAdminRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
		try {
			authorizationService.expectRole(sessionIdentifier, new RoleIdentifier(MONITORING_ROLE_ADMIN));
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public void expectMonitoringViewOrAdminRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, MonitoringServiceException {
		try {
			authorizationService.expectOneOfRoles(sessionIdentifier, new RoleIdentifier(MONITORING_ROLE_ADMIN), new RoleIdentifier(MONITORING_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public void expectMonitoringViewRole(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authorizationService.expectRole(sessionIdentifier, new RoleIdentifier(MONITORING_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public boolean hasMonitoringAdminRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		try {
			return authorizationService.hasRole(sessionIdentifier, new RoleIdentifier(MONITORING_ROLE_ADMIN));
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public boolean hasMonitoringViewOrAdminRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		try {
			return authorizationService.hasOneOfRoles(sessionIdentifier, new RoleIdentifier(MONITORING_ROLE_ADMIN), new RoleIdentifier(MONITORING_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

	@Override
	public boolean hasMonitoringViewRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, MonitoringServiceException {
		try {
			return authorizationService.hasRole(sessionIdentifier, new RoleIdentifier(MONITORING_ROLE_VIEW));
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
	}

}
