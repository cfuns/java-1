package de.benjaminborbe.confluence.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
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
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;
import de.benjaminborbe.confluence.api.ConfluenceSpaceIdentifier;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.confluence.dao.ConfluencePageBean;
import de.benjaminborbe.confluence.dao.ConfluencePageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class ConfluenceServiceImpl implements ConfluenceService {

	private final Logger logger;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	private final DurationUtil durationUtil;

	private final ValidationExecutor validationExecutor;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ConfluenceRefreshCronJob confluenceRefreshCronJob;

	private final AuthorizationService authorizationService;

	private final ConfluencePageDao confluencePageDao;

	@Inject
	public ConfluenceServiceImpl(
			final Logger logger,
			final AuthorizationService authorizationService,
			final ConfluenceInstanceDao confluenceInstanceDao,
			final ConfluencePageDao confluencePageDao,
			final DurationUtil durationUtil,
			final IdGeneratorUUID idGeneratorUUID,
			final ValidationExecutor validationExecutor,
			final ConfluenceRefreshCronJob confluenceRefreshCronJob) {
		this.logger = logger;
		this.confluenceInstanceDao = confluenceInstanceDao;
		this.confluencePageDao = confluencePageDao;
		this.durationUtil = durationUtil;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
		this.confluenceRefreshCronJob = confluenceRefreshCronJob;
		this.authorizationService = authorizationService;
	}

	@Override
	public ConfluenceInstanceIdentifier createConfluenceIntance(final SessionIdentifier sessionIdentifier, final String url, final String username, final String password)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("createConfluenceIntance");
			authorizationService.expectAdminRole(sessionIdentifier);

			final ConfluenceInstanceIdentifier confluenceInstanceIdentifier = createConfluenceInstanceIdentifier(sessionIdentifier, idGeneratorUUID.nextId());
			final ConfluenceInstanceBean confluenceInstance = confluenceInstanceDao.create();
			confluenceInstance.setId(confluenceInstanceIdentifier);
			confluenceInstance.setUrl(url);
			confluenceInstance.setUsername(username);
			confluenceInstance.setPassword(password);

			final ValidationResult errors = validationExecutor.validate(confluenceInstance);
			if (errors.hasErrors()) {
				logger.warn("ConfluenceInstanceBean " + errors.toString());
				throw new ValidationException(errors);
			}
			confluenceInstanceDao.save(confluenceInstance);

			return confluenceInstanceIdentifier;
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void updateConfluenceIntance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier, final String url,
			final String username, final String password) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("updateConfluenceIntance");
			authorizationService.expectAdminRole(sessionIdentifier);

			final ConfluenceInstanceBean confluenceInstance = confluenceInstanceDao.load(confluenceInstanceIdentifier);
			confluenceInstance.setUrl(url);
			confluenceInstance.setUsername(username);
			if (password != null && password.length() > 0) {
				confluenceInstance.setPassword(password);
			}

			final ValidationResult errors = validationExecutor.validate(confluenceInstance);
			if (errors.hasErrors()) {
				logger.warn("ConfluenceInstanceBean " + errors.toString());
				throw new ValidationException(errors);
			}
			confluenceInstanceDao.save(confluenceInstance);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteConfluenceInstance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("deleteConfluenceInstance");
			authorizationService.expectAdminRole(sessionIdentifier);
			confluenceInstanceDao.delete(confluenceInstanceIdentifier);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ConfluenceInstanceIdentifier> getConfluenceInstanceIdentifiers(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstanceIdentifiers");
			authorizationService.expectAdminRole(sessionIdentifier);

			final IdentifierIterator<ConfluenceInstanceIdentifier> i = confluenceInstanceDao.getIdentifierIterator();
			final List<ConfluenceInstanceIdentifier> result = new ArrayList<ConfluenceInstanceIdentifier>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final IdentifierIteratorException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ConfluenceSpaceIdentifier> getConfluenceSpaceIdentifiers(final SessionIdentifier sessionIdentifier, final String confluenceUrl, final String username,
			final String password) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceSpaceIdentifiers");
			authorizationService.expectAdminRole(sessionIdentifier);
			throw new NotImplementedException();
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public ConfluenceInstanceIdentifier createConfluenceInstanceIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws ConfluenceServiceException,
			LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstances");
			authorizationService.expectAdminRole(sessionIdentifier);
			return new ConfluenceInstanceIdentifier(id);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ConfluenceInstance> getConfluenceInstances(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException,
			PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstances");
			authorizationService.expectAdminRole(sessionIdentifier);

			final EntityIterator<ConfluenceInstanceBean> i = confluenceInstanceDao.getEntityIterator();
			final List<ConfluenceInstance> result = new ArrayList<ConfluenceInstance>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public ConfluenceInstance getConfluenceInstance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstances - id: " + confluenceInstanceIdentifier);
			authorizationService.expectAdminRole(sessionIdentifier);

			final ConfluenceInstance confluenceInstance = confluenceInstanceDao.load(confluenceInstanceIdentifier);
			if (confluenceInstance == null) {
				logger.info("confluenceInstance not found with id " + confluenceInstanceIdentifier);
				return null;
			}
			return confluenceInstance;
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void refreshSearchIndex(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("refreshPages");
			authorizationService.expectAdminRole(sessionIdentifier);
			confluenceRefreshCronJob.execute();
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void expireAll(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("refreshPages");
			authorizationService.expectAdminRole(sessionIdentifier);
			final EntityIterator<ConfluencePageBean> i = confluencePageDao.getEntityIterator();
			while (i.hasNext()) {
				final ConfluencePageBean bean = i.next();
				bean.setLastVisit(null);
				confluencePageDao.save(bean);
			}
		}
		catch (final AuthorizationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}
}
