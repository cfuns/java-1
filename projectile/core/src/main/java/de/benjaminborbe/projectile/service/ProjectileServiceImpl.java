package de.benjaminborbe.projectile.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.api.ProjectileTeam;
import de.benjaminborbe.projectile.api.ProjectileTeamDto;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.projectile.dao.ProjectileReportBean;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.projectile.dao.ProjectileTeamBean;
import de.benjaminborbe.projectile.dao.ProjectileTeamDao;
import de.benjaminborbe.projectile.dao.ProjectileTeamUserManyToManyRelation;
import de.benjaminborbe.projectile.util.ProjectileCsvReportImporter;
import de.benjaminborbe.projectile.util.ProjectileMailReportFetcher;
import de.benjaminborbe.projectile.util.ProjectileSlacktimeReportAggregate;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class ProjectileServiceImpl implements ProjectileService {

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final Logger logger;

	private final ProjectileConfig projectileConfig;

	private final ProjectileCsvReportImporter projectileCsvReportImporter;

	private final ProjectileMailReportFetcher projectileMailReportFetcher;

	private final ProjectileReportDao projectileReportDao;

	private final ProjectileTeamDao projectileTeamDao;

	private final DurationUtil durationUtil;

	private final ValidationExecutor validationExecutor;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ProjectileTeamUserManyToManyRelation projectileTeamUserManyToManyRelation;

	private CalendarUtil calendarUtil;

	@Inject
	public ProjectileServiceImpl(
		final Logger logger,
		final ProjectileTeamUserManyToManyRelation projectileTeamUserManyToManyRelation,
		final ValidationExecutor validationExecutor,
		final DurationUtil durationUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final ProjectileConfig projectileConfig,
		final ProjectileCsvReportImporter projectileCsvReportImporter,
		final ProjectileReportDao projectileReportDao,
		final ProjectileTeamDao projectileTeamDao,
		final ProjectileMailReportFetcher projectileMailReportFetcher,
		final IdGeneratorUUID idGeneratorUUID
	) {
		this.logger = logger;
		this.projectileTeamUserManyToManyRelation = projectileTeamUserManyToManyRelation;
		this.validationExecutor = validationExecutor;
		this.durationUtil = durationUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.projectileConfig = projectileConfig;
		this.projectileCsvReportImporter = projectileCsvReportImporter;
		this.projectileReportDao = projectileReportDao;
		this.projectileTeamDao = projectileTeamDao;
		this.projectileMailReportFetcher = projectileMailReportFetcher;
		this.idGeneratorUUID = idGeneratorUUID;
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
		} catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportAllTeams(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
		LoginRequiredException {
		try {
			expectPermission(sessionIdentifier);
			logger.debug("getSlacktimeReportAllTeams");

			final List<ProjectileSlacktimeReport> result = new ArrayList<>();

			final EntityIterator<ProjectileTeamBean> it = projectileTeamDao.getEntityIterator();
			while (it.hasNext()) {
				final ProjectileTeamBean team = it.next();

				final Collection<UserIdentifier> users = getUsersForTeam(sessionIdentifier, team.getId());

				final List<ProjectileSlacktimeReport> userResult = new ArrayList<>();
				final EntityIterator<ProjectileReportBean> i = projectileReportDao.getEntityIterator();
				while (i.hasNext()) {
					final ProjectileReportBean report = i.next();
					boolean match = false;
					for (final UserIdentifier user : users) {
						if (report.getName().equals(user.getId())) {
							match = true;
						}
					}
					if (match) {
						userResult.add(report);
					}
				}
				result.add(aggregateReports(team.getName(), userResult));
			}
			return result;
		} catch (final AuthenticationServiceException | StorageException | EntityIteratorException | AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	private void expectPermission(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException, AuthorizationServiceException,
		PermissionDeniedException {
		authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_VIEW));
	}

	@Override
	public Collection<ProjectileSlacktimeReport> getSlacktimeReportAllUsers(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
		LoginRequiredException {
		try {
			expectProjectileAdminPermission(sessionIdentifier);

			logger.debug("getSlacktimeReportAllUsers");
			final List<ProjectileSlacktimeReport> result = new ArrayList<>();
			final EntityIterator<ProjectileReportBean> i = projectileReportDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException | EntityIteratorException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReportCurrentTeam(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, ProjectileServiceException,
		LoginRequiredException {
		try {
			expectPermission(sessionIdentifier);
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("getSlacktimeReportCurrentTeam for user " + currentUser);

			final ProjectileTeamIdentifier projectileTeamIdentifier = getTeamForUser(sessionIdentifier, currentUser);
			if (projectileTeamIdentifier == null) {
				logger.debug("no team found for user " + currentUser);
				return null;
			}
			final ProjectileTeamBean team = projectileTeamDao.load(projectileTeamIdentifier);
			final Collection<UserIdentifier> users = getUsersForTeam(sessionIdentifier, projectileTeamIdentifier);

			final List<ProjectileSlacktimeReport> result = new ArrayList<>();
			final EntityIterator<ProjectileReportBean> i = projectileReportDao.getEntityIterator();
			while (i.hasNext()) {
				final ProjectileReportBean report = i.next();
				boolean match = false;
				for (final UserIdentifier user : users) {
					if (report.getName().equals(user.getId())) {
						match = true;
					}
				}
				if (match) {
					result.add(report);
				}
			}

			return aggregateReports(team.getName(), result);
		} catch (final AuthenticationServiceException | StorageException | EntityIteratorException | AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	private ProjectileSlacktimeReport aggregateReports(final String name, final List<ProjectileSlacktimeReport> result) {
		return new ProjectileSlacktimeReportAggregate(name, result);
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReportCurrentUser(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException,
		LoginRequiredException {
		try {
			expectPermission(sessionIdentifier);
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			logger.debug("getSlacktimeReportCurrentUser for user " + currentUser);
			return projectileReportDao.getReportForUser(currentUser);
		} catch (final AuthenticationServiceException | StorageException | AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileSlacktimeReport getSlacktimeReportForUser(
		final String token,
		final UserIdentifier userIdentifier
	) throws ProjectileServiceException, PermissionDeniedException {
		try {
			expectAuthToken(token);
			logger.debug("getSlacktimeReportForUser");
			return projectileReportDao.getReportForUser(userIdentifier);
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public void importReport(
		final SessionIdentifier sessionIdentifier,
		final String content,
		final ProjectileSlacktimeReportInterval interval
	) throws ProjectileServiceException,
		PermissionDeniedException, LoginRequiredException, ValidationException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			projectileCsvReportImporter.importCsvReport(calendarUtil.now(), content, interval);
		} catch (final StorageException | AuthorizationServiceException | ParseException e) {
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
	public Collection<ProjectileTeam> listTeams() throws ProjectileServiceException, PermissionDeniedException {
		try {
			final List<ProjectileTeam> result = new ArrayList<>();
			final EntityIterator<ProjectileTeamBean> i = projectileTeamDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final EntityIteratorException | StorageException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileTeamIdentifier getCurrentTeam(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			authenticationService.isLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			return getTeamForUser(sessionIdentifier, userIdentifier);
		} catch (final AuthenticationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileTeamIdentifier createTeam(final SessionIdentifier sessionIdentifier, final ProjectileTeamDto teamDto) throws ProjectileServiceException,
		PermissionDeniedException, ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("createTeam");
			expectProjectileAdminPermission(sessionIdentifier);

			final ProjectileTeamIdentifier id = new ProjectileTeamIdentifier(idGeneratorUUID.nextId());

			final ProjectileTeamBean team = projectileTeamDao.create();
			team.setId(id);
			team.setName(teamDto.getName());

			final ValidationResult errors = validationExecutor.validate(team);
			if (errors.hasErrors()) {
				logger.warn("ConfluenceInstanceBean " + errors.toString());
				throw new ValidationException(errors);
			}
			projectileTeamDao.save(team);

			return id;
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void updateTeam(
		final SessionIdentifier sessionIdentifier,
		final ProjectileTeamDto teamDto
	) throws ProjectileServiceException, PermissionDeniedException,
		LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("updateTeam");
			expectProjectileAdminPermission(sessionIdentifier);

			final ProjectileTeamBean team = projectileTeamDao.load(teamDto.getId());
			team.setName(teamDto.getName());

			final ValidationResult errors = validationExecutor.validate(team);
			if (errors.hasErrors()) {
				logger.warn("ConfluenceInstanceBean " + errors.toString());
				throw new ValidationException(errors);
			}
			projectileTeamDao.save(team);
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteTeam(
		final SessionIdentifier sessionIdentifier,
		final ProjectileTeamIdentifier id
	) throws ProjectileServiceException, PermissionDeniedException,
		LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("deleteTeam");
			expectProjectileAdminPermission(sessionIdentifier);

			projectileTeamUserManyToManyRelation.removeA(id);
			projectileTeamDao.delete(id);
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void expectProjectileAdminPermission(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException {
		try {
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(PERMISSION_ADMIN);
			authorizationService.expectPermission(sessionIdentifier, roleIdentifier);
		} catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public boolean hasProjectileAdminPermission(final SessionIdentifier sessionIdentifier) throws ProjectileServiceException {
		try {
			final PermissionIdentifier roleIdentifier = authorizationService.createPermissionIdentifier(PERMISSION_ADMIN);
			return authorizationService.hasPermission(sessionIdentifier, roleIdentifier);
		} catch (final AuthorizationServiceException e) {
			throw new ProjectileServiceException(e);
		}
	}

	@Override
	public ProjectileTeam getTeam(
		final SessionIdentifier sessionIdentifier,
		final ProjectileTeamIdentifier projectileTeamIdentifier
	) throws ProjectileServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectProjectileAdminPermission(sessionIdentifier);

			logger.debug("getTeam");

			return projectileTeamDao.load(projectileTeamIdentifier);
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void addUserToTeam(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier,
		final ProjectileTeamIdentifier projectileTeamIdentifier
	)
		throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectProjectileAdminPermission(sessionIdentifier);

			logger.debug("addUserToTeam");

			projectileTeamUserManyToManyRelation.removeB(userIdentifier);
			projectileTeamUserManyToManyRelation.add(projectileTeamIdentifier, userIdentifier);
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public void removeUserFromTeam(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier,
		final ProjectileTeamIdentifier projectileTeamIdentifier
	)
		throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectProjectileAdminPermission(sessionIdentifier);

			logger.debug("removeUserFromTeam");

			projectileTeamUserManyToManyRelation.remove(projectileTeamIdentifier, userIdentifier);
		} catch (final StorageException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public ProjectileTeamIdentifier getTeamForUser(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws ProjectileServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.isLoggedIn(sessionIdentifier);
			logger.debug("getTeamForUser");

			final StorageIterator i = projectileTeamUserManyToManyRelation.getB(userIdentifier);
			while (i.hasNext()) {
				return new ProjectileTeamIdentifier(i.next().getString());
			}
			return null;
		} catch (final StorageException | AuthenticationServiceException | UnsupportedEncodingException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<UserIdentifier> getUsersForTeam(final SessionIdentifier sessionIdentifier, final ProjectileTeamIdentifier projectileTeamIdentifier)
		throws ProjectileServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.isLoggedIn(sessionIdentifier);
			logger.debug("removeUserFromTeam");

			final List<UserIdentifier> result = new ArrayList<>();

			final StorageIterator i = projectileTeamUserManyToManyRelation.getA(projectileTeamIdentifier);
			while (i.hasNext()) {
				result.add(new UserIdentifier(i.next().getString()));
			}
			return result;
		} catch (final StorageException | AuthenticationServiceException | UnsupportedEncodingException e) {
			throw new ProjectileServiceException(e);
		} finally {
			logger.trace("duration " + duration.getTime());
		}
	}

}
