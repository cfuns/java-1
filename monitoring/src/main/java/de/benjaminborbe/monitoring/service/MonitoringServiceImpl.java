package de.benjaminborbe.monitoring.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeDto;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;
import de.benjaminborbe.monitoring.dao.MonitoringNodeDao;
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

	private final AuthorizationService authorizationService;

	private final DurationUtil durationUtil;

	private final MonitoringNodeDao monitoringNodeDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final MonitoringCheckFactory monitoringCheckFactory;

	@Inject
	public MonitoringServiceImpl(
			final Logger logger,
			final MonitoringCheckFactory monitoringCheckFactory,
			final ValidationExecutor validationExecutor,
			final IdGeneratorUUID idGeneratorUUID,
			final AuthorizationService authorizationService,
			final DurationUtil durationUtil,
			final MonitoringNodeDao monitoringNodeDao) {
		this.logger = logger;
		this.monitoringCheckFactory = monitoringCheckFactory;
		this.validationExecutor = validationExecutor;
		this.idGeneratorUUID = idGeneratorUUID;
		this.authorizationService = authorizationService;
		this.durationUtil = durationUtil;
		this.monitoringNodeDao = monitoringNodeDao;
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

	@Override
	public Collection<String> getRequireParameter(final SessionIdentifier sessionIdentifier, final MonitoringCheckType monitoringCheckType) throws MonitoringServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			final MonitoringCheck check = monitoringCheckFactory.get(monitoringCheckType);
			return check.getRequireParameters();
		}
		catch (final AuthorizationServiceException e) {
			throw new MonitoringServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

}
