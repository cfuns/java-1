package de.benjaminborbe.authorization.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import de.benjaminborbe.authorization.permission.PermissionBean;
import de.benjaminborbe.authorization.permission.PermissionDao;
import de.benjaminborbe.authorization.permissionrole.PermissionRoleManyToManyRelation;
import de.benjaminborbe.authorization.role.RoleBean;
import de.benjaminborbe.authorization.role.RoleDao;
import de.benjaminborbe.authorization.userrole.UserRoleManyToManyRelation;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;

@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

	private static final String ADMIN_ROLE = "admin";

	private final Logger logger;

	private final RoleDao roleDao;

	private final PermissionDao permissionDao;

	private final AuthenticationService authenticationService;

	private final UserRoleManyToManyRelation userRoleManyToManyRelation;

	private final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation;

	@Inject
	public AuthorizationServiceImpl(
			final Logger logger,
			final AuthenticationService authenticationService,
			final RoleDao roleDao,
			final PermissionDao permissionDao,
			final UserRoleManyToManyRelation userRoleManyToManyRelation,
			final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.roleDao = roleDao;
		this.permissionDao = permissionDao;
		this.userRoleManyToManyRelation = userRoleManyToManyRelation;
		this.permissionRoleManyToManyRelation = permissionRoleManyToManyRelation;
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {

		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return true;
			}
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}

		try {
			logger.trace("hasRole " + roleIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			return hasRole(userIdentifier, roleIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException {

		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return true;
			}
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}

		try {
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}

			for (final RoleIdentifier roleIdentifier : roleList()) {
				if (hasRole(userIdentifier, roleIdentifier)) {
					if (permissionRoleManyToManyRelation.exists(permissionIdentifier, roleIdentifier)) {
						return true;
					}
				}
			}

			return authenticationService.isSuperAdmin(sessionIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException("AuthenticationServiceException", e);
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void expectRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthorizationServiceException, PermissionDeniedException {
		try {
			if (authenticationService.isSuperAdmin(sessionIdentifier)) {
				return;
			}
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
		}
		if (!hasRole(sessionIdentifier, roleIdentifier)) {
			throw new PermissionDeniedException("no role " + roleIdentifier);
		}
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException,
			PermissionDeniedException {
		if (!hasPermission(sessionIdentifier, permissionIdentifier)) {
			throw new PermissionDeniedException("no permission " + permissionIdentifier);
		}
	}

	@Override
	public boolean createRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws PermissionDeniedException, AuthorizationServiceException {
		try {
			expectPermission(sessionIdentifier, new PermissionIdentifier("createRole"));

			if (roleDao.findByRolename(roleIdentifier) != null) {
				logger.info("role " + roleIdentifier + " already exists");
				return false;
			}
			final RoleBean role = roleDao.findOrCreateByRolename(roleIdentifier);
			roleDao.save(role);
			return true;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean addUserRole(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException {
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
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean removeUserRole(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final RoleIdentifier roleIdentifier)
			throws PermissionDeniedException, AuthorizationServiceException {
		try {
			expectAdminRole(sessionIdentifier);

			logger.info("removeUserRole " + userIdentifier + " " + roleIdentifier);
			userRoleManyToManyRelation.remove(userIdentifier, roleIdentifier);

			return true;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public RoleIdentifier createRoleIdentifier(final String rolename) {
		return new RoleIdentifier(rolename);
	}

	@Override
	public boolean hasRole(final UserIdentifier userIdentifier, final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		try {
			final RoleBean role = roleDao.findByRolename(roleIdentifier);
			if (role != null && userRoleManyToManyRelation.exists(userIdentifier, roleIdentifier)) {
				return true;
			}
			return authenticationService.isSuperAdmin(userIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<RoleIdentifier> roleList() throws AuthorizationServiceException {
		try {
			final Set<RoleIdentifier> result = new HashSet<RoleIdentifier>();
			final EntityIterator<RoleBean> i = roleDao.getEntityIterator();
			while (i.hasNext()) {
				final RoleBean role = i.next();
				result.add(role.getId());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public PermissionIdentifier createPermissionIdentifier(final String permissionName) {
		return new PermissionIdentifier(permissionName);
	}

	@Override
	public Collection<PermissionIdentifier> permissionList() throws AuthorizationServiceException {
		try {
			final Set<PermissionIdentifier> result = new HashSet<PermissionIdentifier>();
			final EntityIterator<PermissionBean> i = permissionDao.getEntityIterator();
			while (i.hasNext()) {
				final PermissionBean permission = i.next();
				result.add(permission.getId());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean addPermissionRole(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier, final RoleIdentifier roleIdentifier)
			throws PermissionDeniedException, AuthorizationServiceException {
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
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean removePermissionRole(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier, final RoleIdentifier roleIdentifier)
			throws PermissionDeniedException, AuthorizationServiceException {
		try {
			expectAdminRole(sessionIdentifier);

			logger.info("removePermissionRole " + permissionIdentifier + " " + roleIdentifier);
			permissionRoleManyToManyRelation.remove(permissionIdentifier, roleIdentifier);

			return true;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<PermissionIdentifier> permissionList(final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		try {
			logger.info("permissionList for role: " + roleIdentifier);

			final Set<PermissionIdentifier> result = new HashSet<PermissionIdentifier>();
			for (final PermissionIdentifier permissionIdentifier : permissionList()) {
				if (permissionRoleManyToManyRelation.exists(permissionIdentifier, roleIdentifier)) {
					result.add(permissionIdentifier);
				}
			}

			return result;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean existsRole(final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		try {
			return roleDao.exists(roleIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean existsPermission(final PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException {
		try {
			return permissionDao.exists(permissionIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void expectAdminRole(final SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, PermissionDeniedException {
		expectRole(sessionIdentifier, new RoleIdentifier(ADMIN_ROLE));
	}

	@Override
	public boolean hasAdminRole(final SessionIdentifier sessionIdentifier) throws AuthorizationServiceException {
		return hasRole(sessionIdentifier, new RoleIdentifier(ADMIN_ROLE));
	}

	@Override
	public void expectUser(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier) throws AuthorizationServiceException, PermissionDeniedException,
			LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier currentUser = authenticationService.getCurrentUser(sessionIdentifier);
			if (userIdentifier == null || currentUser == null || !userIdentifier.equals(currentUser)) {
				throw new PermissionDeniedException("expect user " + userIdentifier + " but was " + currentUser);
			}
		}
		catch (final AuthenticationServiceException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<UserIdentifier> getUserWithRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthorizationServiceException {
		try {
			final StorageIterator a = userRoleManyToManyRelation.getB(roleIdentifier);
			final List<UserIdentifier> result = new ArrayList<UserIdentifier>();
			while (a.hasNext()) {
				result.add(new UserIdentifier(a.nextString()));
			}
			return result;
		}
		catch (final StorageException e) {
			throw new AuthorizationServiceException(e.getClass().getSimpleName(), e);
		}
		finally {
		}
	}
}
