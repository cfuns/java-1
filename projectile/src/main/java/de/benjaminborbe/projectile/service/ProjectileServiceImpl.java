package de.benjaminborbe.projectile.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.projectile.util.ProjectileCsvReportImporter;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class ProjectileServiceImpl implements ProjectileService {

	private final Logger logger;

	private final ProjectileConfig projectileConfig;

	private final ProjectileReportDao projectileReportDao;

	private final AuthenticationService authenticationService;

	private final ProjectileCsvReportImporter projectileCsvReportImporter;

	@Inject
	public ProjectileServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final ProjectileConfig projectileConfig,
			final ProjectileCsvReportImporter projectileCsvReportImporter,
			final ProjectileReportDao projectileReportDao) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.projectileConfig = projectileConfig;
		this.projectileCsvReportImporter = projectileCsvReportImporter;
		this.projectileReportDao = projectileReportDao;
	}

	@Override
	public boolean validateAuthToken(final String token) throws ProjectileServiceException {
		logger.debug("validateAuthToken");
		final String authToken = projectileConfig.getAuthToken();
		final boolean result = authToken != null && token != null && authToken.equals(token);
		logger.debug("validateAuthToken - result: " + result);
		return result;
	}

	@Override
	public void expectAuthToken(final String token) throws ProjectileServiceException, PermissionDeniedException {
		if (!validateAuthToken(token)) {
			throw new PermissionDeniedException("token invalid");
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReport(final String token, final UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		try {
			expectAuthToken(token);
			logger.debug("getSlacktimeReport");
			return projectileReportDao.getReportForUser(userIdentifier);
		}
		catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public void importReport(final SessionIdentifier sessionIdentifier, final String content, final ProjectileSlacktimeReportInterval interval) throws ProjectileServiceException,
			PermissionDeniedException, LoginRequiredException, ValidationException {
		try {
			authenticationService.expectSuperAdmin(sessionIdentifier);
			projectileCsvReportImporter.importCsvReport(content, interval);
		}
		catch (final AuthenticationServiceException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final SuperAdminRequiredException e) {
			throw new PermissionDeniedException(e);
		}
		catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final ParseException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReport(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			logger.debug("getSlacktimeReport");
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			return projectileReportDao.getReportForUser(currentUser);
		}
		catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

}
