package de.benjaminborbe.confluence.service;

import java.util.Collection;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.api.ConfluenceService;
import de.benjaminborbe.confluence.api.ConfluenceServiceException;
import de.benjaminborbe.confluence.api.ConfluenceSpaceIdentifier;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class ConfluenceServiceImpl implements ConfluenceService {

	private final Logger logger;

	private final ConfluenceInstanceDao confluenceInstanceDao;

	private final AuthenticationService authenticationService;

	private final DurationUtil durationUtil;

	private final ValidationExecutor validationExecutor;

	private final IdGeneratorUUID idGeneratorUUID;

	@Inject
	public ConfluenceServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final ConfluenceInstanceDao confluenceInstanceDao,
			final DurationUtil durationUtil,
			final IdGeneratorUUID idGeneratorUUID,
			final ValidationExecutor validationExecutor) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.confluenceInstanceDao = confluenceInstanceDao;
		this.durationUtil = durationUtil;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public ConfluenceInstanceIdentifier createConfluenceIntance(final SessionIdentifier sessionIdentifier, final String url, final String username, final String password)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("createConfluenceIntance");
			authenticationService.expectSuperAdmin(sessionIdentifier);

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
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void updateConfluenceIntance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier, final String url,
			final String username, final String password) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, ValidationException,
			SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("updateConfluenceIntance");
			authenticationService.expectSuperAdmin(sessionIdentifier);

			final ConfluenceInstanceBean confluenceInstance = confluenceInstanceDao.load(confluenceInstanceIdentifier);
			confluenceInstance.setUrl(url);
			confluenceInstance.setUsername(username);
			confluenceInstance.setPassword(password);

			final ValidationResult errors = validationExecutor.validate(confluenceInstance);
			if (errors.hasErrors()) {
				logger.warn("ConfluenceInstanceBean " + errors.toString());
				throw new ValidationException(errors);
			}
			confluenceInstanceDao.save(confluenceInstance);
		}
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteConfluenceInstance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier)
			throws ConfluenceServiceException, LoginRequiredException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("deleteConfluenceInstance");
			authenticationService.expectSuperAdmin(sessionIdentifier);
			confluenceInstanceDao.delete(confluenceInstanceIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		catch (final StorageException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ConfluenceInstanceIdentifier> getConfluenceInstanceIdentifiers(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException,
			LoginRequiredException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstanceIdentifiers");
			authenticationService.expectSuperAdmin(sessionIdentifier);
			return null;
		}
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ConfluenceSpaceIdentifier> getConfluenceSpaceIdentifiers(final SessionIdentifier sessionIdentifier, final String confluenceUrl, final String username,
			final String password) throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceSpaceIdentifiers");
			authenticationService.expectSuperAdmin(sessionIdentifier);
			throw new NotImplementedException();
		}
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public ConfluenceInstanceIdentifier createConfluenceInstanceIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws ConfluenceServiceException,
			LoginRequiredException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstances");
			authenticationService.expectSuperAdmin(sessionIdentifier);
			return new ConfluenceInstanceIdentifier(id);
		}
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ConfluenceInstance> getConfluenceInstances(final SessionIdentifier sessionIdentifier) throws ConfluenceServiceException, LoginRequiredException,
			PermissionDeniedException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstances");
			authenticationService.expectSuperAdmin(sessionIdentifier);
			throw new NotImplementedException();
		}
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public ConfluenceInstance getConfluenceInstance(final SessionIdentifier sessionIdentifier, final ConfluenceInstanceIdentifier confluenceInstanceIdentifier)
			throws ConfluenceServiceException, LoginRequiredException, PermissionDeniedException, SuperAdminRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getConfluenceInstances - id: " + confluenceInstanceIdentifier);
			authenticationService.expectSuperAdmin(sessionIdentifier);

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
		catch (final AuthenticationServiceException e) {
			throw new ConfluenceServiceException(e);
		}
		finally {
			logger.trace("duration " + duration.getTime());
		}
	}
}
