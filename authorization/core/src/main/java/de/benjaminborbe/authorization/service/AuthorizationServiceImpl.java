package de.benjaminborbe.authorization.service;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.dao.PermissionBean;
import de.benjaminborbe.authorization.dao.PermissionDao;
import de.benjaminborbe.authorization.dao.PermissionRoleManyToManyRelation;
import de.benjaminborbe.authorization.dao.RoleBean;
import de.benjaminborbe.authorization.dao.RoleDao;
import de.benjaminborbe.authorization.dao.UserRoleManyToManyRelation;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

	private static final int DURATION_WARN = 300;

	private final AuthenticationService authenticationService;

	private final Logger logger;

	private final PermissionDao permissionDao;

	private final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation;

	private final DurationUtil durationUtil;

	private final ThreadRunner threadRunner;

	private final RoleDao roleDao;

	private final UserRoleManyToManyRelation userRoleManyToManyRelation;

	@Inject
	public AuthorizationServiceImpl(
		final Logger logger,
		final AuthenticationService authenticationService,
		final RoleDao roleDao,
		final PermissionDao permissionDao,
		final UserRoleManyToManyRelation userRoleManyToManyRelation,
		final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation,
		final DurationUtil durationUtil,
		final ThreadRunner threadRunner
	) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.roleDao = roleDao;
		this.permissionDao = permissionDao;
		this.userRoleManyToManyRelation = userRoleManyToManyRelation;
		this.permissionRoleManyToManyRelation = permissionRoleManyToManyRelation;
		this.durationUtil = durationUtil;
		this.threadRunner = threadRunner;
	}

	@Override
	public boolean addPermissionRole(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier permissionIdentifier,
		final RoleIdentifier roleIdentifier
	) throws PermissionDeniedException, AuthorizationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);

			if (!existsPermission(permissionIdentifier)) {
				throw new AuthorizationServiceException("permission " + permissionIdentifier + " does not exists");
			}
			if (!existsRole(roleIdentifier)) {
				throw new AuthorizationServiceException("role " + roleIdentifier + " does not exists");
			}

			logger.info("addPermissionRole " + permissionIdentifier + " " + roleIdentifier);
			permissionRoleManyToManyRelation.add(permissionIdentifier, roleIdentifier);

			return true;
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean addUserRole(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier,
		final RoleIdentifier roleIdentifier
	) throws PermissionDeniedException, AuthorizationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);

			if (!authenticationService.existsUser(userIdentifier)) {
				throw new AuthorizationServiceException("user " + userIdentifier + " does not exists");
			}
			if (!existsRole(roleIdentifier)) {
				throw new AuthorizationServiceException("role " + roleIdentifier + " does not exists");
			}

			logger.info("addUserRole " + userIdentifier + " " + roleIdentifier);
			userRoleManyToManyRelation.add(userIdentifier, roleIdentifier);

			return true;
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PermissionIdentifier createPermissionIdentifier(final String permissionName) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (permissionName != null) {
				final PermissionIdentifier id = new PermissionIdentifier(permissionName);
				permissionDao.findOrCreate(id);
				return id;
			} else {
				return null;
			}
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean createRole(
		final SessionIdentifier sessionIdentifier,
		final RoleIdentifier roleIdentifier
	) throws PermissionDeniedException, AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectPermission(sessionIdentifier, new PermissionIdentifier(PERMISSION_CREATE_ROLE));

			if (roleDao.findByRolename(roleIdentifier) != null) {
				logger.info("role " + roleIdentifier + " already exists");
				return false;
			}
			final RoleBean role = roleDao.findOrCreateByRolename(roleIdentifier);
			roleDao.save(role);
			return true;
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public RoleIdentifier createRoleIdentifier(final String rolename) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("createRoleIdentifier - role: " + rolename);
			if (rolename != null) {
				final RoleIdentifier id = new RoleIdentifier(rolename);
				roleDao.findOrCreate(id);
				return id;
			} else {
				return null;
			}
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean existsPermission(final PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return permissionDao.exists(permissionIdentifier);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private boolean existsRole(final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		try {
			return roleDao.exists(roleIdentifier);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		}
	}

	@Override
	public void expectAdminRole(final SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectRole(sessionIdentifier, new RoleIdentifier(ROLE_ADMIN));
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException,
		PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (!hasPermission(sessionIdentifier, permissionIdentifier)) {
				throw new PermissionDeniedException("no permission " + permissionIdentifier);
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void expectRole(
		final SessionIdentifier sessionIdentifier,
		final RoleIdentifier roleIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException,
		LoginRequiredException {
		expectOneOfRoles(sessionIdentifier, roleIdentifier);
	}

	@Override
	public void expectUser(final SessionIdentifier sessionIdentifier, final Collection<UserIdentifier> userIdentifiers) throws AuthorizationServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			expectUser(currentUser, userIdentifiers);
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectUser(
		final UserIdentifier currentUser, final Collection<UserIdentifier> userIdentifiers
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (currentUser == null) {
				throw new LoginRequiredException("user not logged in");
			}
			boolean match = false;
			final List<String> usernames = new ArrayList<String>();
			for (final UserIdentifier userIdentifier : userIdentifiers) {
				usernames.add(userIdentifier.getId());
				if (userIdentifier.equals(currentUser)) {
					match = true;
				}
			}
			if (!match) {
				throw new PermissionDeniedException("expect user " + StringUtils.join(usernames, " or ") + " but was " + currentUser);
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectUser(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectUser(sessionIdentifier, Arrays.asList(userIdentifier));
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectUser(final UserIdentifier currentUser, final UserIdentifier userIdentifier) throws AuthorizationServiceException, PermissionDeniedException,
		LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectUser(currentUser, Arrays.asList(userIdentifier));
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<UserIdentifier> getUsersWithRole(final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final StorageIterator a = userRoleManyToManyRelation.getB(roleIdentifier);
			final List<UserIdentifier> result = new ArrayList<UserIdentifier>();
			while (a.hasNext()) {
				result.add(new UserIdentifier(a.next().getString()));
			}
			return result;
		} catch (final UnsupportedEncodingException e) {
			throw new AuthorizationServiceException(e);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasAdminRole(final SessionIdentifier sessionIdentifier) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return hasRole(sessionIdentifier, new RoleIdentifier(ROLE_ADMIN));
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasPermission(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier permissionIdentifier
	) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return true;
			}
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}

			for (final RoleIdentifier roleIdentifier : getRoles()) {
				if (hasRole(userIdentifier, roleIdentifier)) {
					if (permissionRoleManyToManyRelation.exists(permissionIdentifier, roleIdentifier)) {
						return true;
					}
				}
			}

			return authenticationService.isSuperAdmin(sessionIdentifier);
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException("AuthenticationServiceException", e);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	protected boolean hasRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		return hasOneOfRoles(sessionIdentifier, roleIdentifier);
	}

	protected boolean hasRole(final UserIdentifier userIdentifier, final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		try {
			logger.trace("hasRole - user: " + userIdentifier + " role: " + roleIdentifier);
			if (userIdentifier != null && ROLE_LOGGED_IN.equals(roleIdentifier.getId())) {
				return true;
			}
			if (userIdentifier == null && ROLE_LOGGED_OUT.equals(roleIdentifier.getId())) {
				return true;
			}

			final RoleBean role = roleDao.findByRolename(roleIdentifier);
			if (role != null && userRoleManyToManyRelation.exists(userIdentifier, roleIdentifier)) {
				return true;
			}
			return authenticationService.isSuperAdmin(userIdentifier);
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		}
	}

	@Override
	public Collection<PermissionIdentifier> getPermissions() throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final Set<PermissionIdentifier> result = new HashSet<PermissionIdentifier>();
			final EntityIterator<PermissionBean> i = permissionDao.getEntityIterator();
			while (i.hasNext()) {
				final PermissionBean permission = i.next();
				result.add(permission.getId());
			}
			return result;
		} catch (final EntityIteratorException e) {
			throw new AuthorizationServiceException(e);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<PermissionIdentifier> getPermissions(final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.info("permissionList for role: " + roleIdentifier);

			final Set<PermissionIdentifier> result = new HashSet<PermissionIdentifier>();
			for (final PermissionIdentifier permissionIdentifier : getPermissions()) {
				if (permissionRoleManyToManyRelation.exists(permissionIdentifier, roleIdentifier)) {
					result.add(permissionIdentifier);
				}
			}
			return result;
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean removePermissionRole(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier permissionIdentifier,
		final RoleIdentifier roleIdentifier
	) throws PermissionDeniedException, AuthorizationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);

			logger.info("removePermissionRole " + permissionIdentifier + " " + roleIdentifier);
			permissionRoleManyToManyRelation.remove(permissionIdentifier, roleIdentifier);

			return true;
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean removeUserRole(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final RoleIdentifier roleIdentifier)
		throws PermissionDeniedException, AuthorizationServiceException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);

			logger.info("removeUserRole " + userIdentifier + " " + roleIdentifier);
			userRoleManyToManyRelation.remove(userIdentifier, roleIdentifier);

			return true;
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<RoleIdentifier> getRoles() throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final Set<RoleIdentifier> result = new HashSet<RoleIdentifier>();
			final EntityIterator<RoleBean> i = roleDao.getEntityIterator();
			while (i.hasNext()) {
				final RoleBean role = i.next();
				result.add(role.getId());
			}

			result.add(new RoleIdentifier(ROLE_LOGGED_IN));
			result.add(new RoleIdentifier(ROLE_LOGGED_OUT));

			return result;
		} catch (final EntityIteratorException e) {
			throw new AuthorizationServiceException(e);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deleteRole(
		final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);
			logger.debug("deleteRole - role: " + roleIdentifier);

			userRoleManyToManyRelation.removeB(roleIdentifier);
			permissionRoleManyToManyRelation.removeB(roleIdentifier);
			roleDao.delete(roleIdentifier);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void expectOneOfRoles(final SessionIdentifier sessionIdentifier, final RoleIdentifier... roleIdentifiers) throws AuthorizationServiceException,
		PermissionDeniedException, LoginRequiredException {
		if (!hasOneOfRoles(sessionIdentifier, roleIdentifiers)) {
			throw new PermissionDeniedException("no role " + roleIdentifiers);
		}
	}

	private boolean hasOneOfRoles(final SessionIdentifier sessionIdentifier, final RoleIdentifier... roleIdentifiers) throws AuthorizationServiceException {
		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return true;
			}
			logger.trace("hasOneOfRoles " + StringUtils.join(roleIdentifiers, ","));
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			final List<ThreadResult<Boolean>> threadResults = new ArrayList<ThreadResult<Boolean>>();
			final List<Thread> threads = new ArrayList<Thread>();
			for (final RoleIdentifier roleIdentifier : roleIdentifiers) {
				final ThreadResult<Boolean> threadResult = new ThreadResult<Boolean>();
				threadResults.add(threadResult);
				threadRunner.run("hasRole", new HasRoleRunnable(threadResult, userIdentifier, roleIdentifier));
			}
			for (final Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {

				}
			}
			for (final ThreadResult threadResult : threadResults) {
				if (Boolean.TRUE.equals(threadResult.get())) {
					return true;
				}
			}
			return false;
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e);
		}
	}

	@Override
	public void expectOneOfPermissions(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier... permissionIdentifiers
	) throws AuthorizationServiceException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (!hasOneOfPermissions(sessionIdentifier, permissionIdentifiers)) {
				throw new PermissionDeniedException("no permission " + permissionIdentifiers);
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasOneOfPermissions(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier... permissionIdentifiers
	) throws AuthorizationServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return true;
			}
			for (final PermissionIdentifier permissionIdentifier : permissionIdentifiers) {
				if (hasPermission(sessionIdentifier, permissionIdentifier)) {
					return true;
				}
			}
			return false;
		} catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<RoleIdentifier> getRoles(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);
			logger.debug("getRoles for user: " + userIdentifier);

			final List<RoleIdentifier> roles = new ArrayList<RoleIdentifier>();
			for (final RoleIdentifier roleIdentifier : getRoles()) {
				if (hasRole(userIdentifier, roleIdentifier)) {
					roles.add(roleIdentifier);
				}
			}
			return roles;
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deletePermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);
			logger.debug("deletePermission- permission: " + permissionIdentifier);

			permissionRoleManyToManyRelation.removeA(permissionIdentifier);
			permissionDao.delete(permissionIdentifier);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<RoleIdentifier> getRoles(
		final SessionIdentifier sessionIdentifier,
		final PermissionIdentifier permissionIdentifier
	) throws AuthorizationServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			expectAdminRole(sessionIdentifier);
			logger.debug("getRoles for permission: " + permissionIdentifier);

			final List<RoleIdentifier> roles = new ArrayList<RoleIdentifier>();
			final StorageIterator i = permissionRoleManyToManyRelation.getA(permissionIdentifier);
			while (i.hasNext()) {
				final StorageValue v = i.next();
				roles.add(new RoleIdentifier(v.getString()));
			}
			return roles;
		} catch (final UnsupportedEncodingException e) {
			throw new AuthorizationServiceException(e);
		} catch (final StorageException e) {
			throw new AuthorizationServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private class HasRoleRunnable implements Runnable {

		private final ThreadResult<Boolean> threadResult;

		private final UserIdentifier userIdentifier;

		private final RoleIdentifier roleIdentifier;

		public HasRoleRunnable(final ThreadResult<Boolean> threadResult, final UserIdentifier userIdentifier, final RoleIdentifier roleIdentifier) {
			this.threadResult = threadResult;
			this.userIdentifier = userIdentifier;
			this.roleIdentifier = roleIdentifier;
		}

		@Override
		public void run() {
			try {
				threadResult.set(hasRole(userIdentifier, roleIdentifier));
			} catch (AuthorizationServiceException e) {
				logger.debug(e.getClass().getName(), e);
			}
		}
	}
}
