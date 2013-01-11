package de.benjaminborbe.projectile.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.api.TeamDto;
import de.benjaminborbe.projectile.api.TeamIdentifier;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.dao.ProjectileReportBean;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.projectile.util.ProjectileCsvReportImporter;
import de.benjaminborbe.projectile.util.ProjectileMailReportFetcher;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class ProjectileServiceImpl implements ProjectileService {

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final Logger logger;

	private final ProjectileConfig projectileConfig;

	private final ProjectileCsvReportImporter projectileCsvReportImporter;

	private final ProjectileMailReportFetcher projectileMailReportFetcher;

	private final ProjectileReportDao projectileReportDao;

	@Inject
	public ProjectileServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final ProjectileConfig projectileConfig,
			final ProjectileCsvReportImporter projectileCsvReportImporter,
			final ProjectileReportDao projectileReportDao,
			final ProjectileMailReportFetcher projectileMailReportFetcher) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.projectileConfig = projectileConfig;
		this.projectileCsvReportImporter = projectileCsvReportImporter;
		this.projectileReportDao = projectileReportDao;
		this.projectileMailReportFetcher = projectileMailReportFetcher;
	}

	@Override
	public void expectAuthToken(final String token) throws ProjectileServiceException, PermissionDeniedException {
		if (!validateAuthToken(token)) {
			throw new PermissionDeniedException("token invalid");
		}
	}

	@Override
	public void fetchMailReport(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException, LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("fetchMailReport");
			projectileMailReportFetcher.fetch();
		}
		catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportAllTeams(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getSlacktimeReportAllTeams");
			final List<ProjectileSlacktimeReport> result = new ArrayList<ProjectileSlacktimeReport>();
			return result;
		}
		catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportAllUsers(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
			LoginRequiredException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			logger.debug("getSlacktimeReportAllUsers");
			final List<ProjectileSlacktimeReport> result = new ArrayList<ProjectileSlacktimeReport>();
			final EntityIterator<ProjectileReportBean> i = projectileReportDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportCurrentTeam(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException,
			ProjectileServiceException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("getSlacktimeReportCurrentTeam for user " + currentUser);

			final List<ProjectileSlacktimeReport> result = new ArrayList<ProjectileSlacktimeReport>();
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReportCurrentUser(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException,
			LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("getSlacktimeReportCurrentUser for user " + currentUser);
			return projectileReportDao.getReportForUser(currentUser);
		}
		catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final AuthenticationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReportForUser(final String token, final UserIdentifier userIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		try {
			expectAuthToken(token);
			logger.debug("getSlacktimeReportForUser");
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
			authorizationService.expectAdminRole(sessionIdentifier);

			projectileCsvReportImporter.importCsvReport(content, interval);
		}
		catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final ParseException e) {
			throw new ProjectileServiceException(e);
		}
		catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
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
	public Collection<TeamIdentifier> listTeams(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public TeamIdentifier getCurrentTeam(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public TeamIdentifier createTeam(final SessionIdentifier sessionIdentifier, final TeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public void updateTeam(final SessionIdentifier sessionIdentifier, final TeamDto teamDto) throws ProjectileServiceException, PermissionDeniedException {
	}

	@Override
	public void deleteTeam(final SessionIdentifier sessionIdentifier, final TeamIdentifier id) throws ProjectileServiceException, PermissionDeniedException {
	}
}
