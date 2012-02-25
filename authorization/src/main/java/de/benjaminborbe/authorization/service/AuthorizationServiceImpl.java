package de.benjaminborbe.authorization.service;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.Role;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.permission.PermissionBean;
import de.benjaminborbe.authorization.permission.PermissionDao;
import de.benjaminborbe.authorization.role.RoleBean;
import de.benjaminborbe.authorization.role.RoleDao;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

	private static final String ADMIN_USERNAME = "admin";

	private final Logger logger;

	private final RoleDao roleDao;

	private final AuthenticationService authenticationService;

	private final PermissionDao permissionDao;

	@Inject
	public AuthorizationServiceImpl(final Logger logger, final AuthenticationService authenticationService, final RoleDao roleDao, final PermissionDao permissionDao) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.roleDao = roleDao;
		this.permissionDao = permissionDao;
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthenticationServiceException {
		try {
			logger.trace("hasRole " + roleIdentifier);
			final RoleBean role = roleDao.findByRolename(roleIdentifier);
			if (role == null) {
				return false;
			}
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final String username = userIdentifier.getId();
			return ADMIN_USERNAME.equals(username);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException("StorageException", e);
		}
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthenticationServiceException {
		try {
			final PermissionBean permission = permissionDao.load(permissionIdentifier);
			if (permission == null) {
				return false;
			}
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final String username = userIdentifier.getId();
			return ADMIN_USERNAME.equals(username);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException("StorageException", e);
		}
	}

	@Override
	public void expectRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthenticationServiceException, PermissionDeniedException {
		if (!hasRole(sessionIdentifier, roleIdentifier)) {
			throw new PermissionDeniedException("no role " + roleIdentifier);
		}
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthenticationServiceException,
			PermissionDeniedException {
		if (!hasPermission(sessionIdentifier, permissionIdentifier)) {
			throw new PermissionDeniedException("no permission " + permissionIdentifier);
		}
	}

	@Override
	public Collection<Role> getRoles() throws AuthenticationServiceException {
		try {
			return new HashSet<Role>(roleDao.getAll());
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException("StorageException", e);
		}
	}

}
