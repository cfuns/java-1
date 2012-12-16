package de.benjaminborbe.checklist.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;

@Singleton
public class ChecklistServiceImpl implements ChecklistService {

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final DurationUtil durationUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public ChecklistServiceImpl(final Logger logger, final DurationUtil durationUtil, final AuthenticationService authenticationService) {
		this.logger = logger;
		this.durationUtil = durationUtil;
		this.authenticationService = authenticationService;
	}

	@Override
	public void delete(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistList read(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
			return null;
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistListIdentifier create(final SessionIdentifier sessionIdentifier, final ChecklistList object) throws ChecklistServiceException, PermissionDeniedException,
			ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
			return null;
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void update(final SessionIdentifier sessionIdentifier, final ChecklistList object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void delete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistEntry read(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
			return null;
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistEntryIdentifier create(final SessionIdentifier sessionIdentifier, final ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException,
			ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
			return null;
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void update(final SessionIdentifier sessionIdentifier, final ChecklistEntry object) throws ChecklistServiceException, PermissionDeniedException, ValidationException,
			LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("");
		}
		catch (final AuthenticationServiceException e) {
			throw new ChecklistServiceException(e);
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
