package de.benjaminborbe.authorization.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.util.RoleBean;
import de.benjaminborbe.authorization.util.RoleDao;

@Singleton
public class AuthorizationServiceImpl implements AuthorizationService {

	private final Logger logger;

	private final RoleDao roleDao;

	private final AuthenticationService authenticationService;

	@Inject
	public AuthorizationServiceImpl(final Logger logger, final AuthenticationService authenticationService, final RoleDao roleDao) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.roleDao = roleDao;
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final String roleName) throws AuthenticationServiceException {
		logger.debug("hasRole " + roleName);
		final RoleBean role = roleDao.findByRolename(roleName);
		if (role == null) {
			return false;
		}
		final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
		if (userIdentifier == null) {
			return false;
		}
		final String username = userIdentifier.getId();
		return "bborbe".equals(username);
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthenticationServiceException {
		return roleIdentifier != null && hasRole(sessionIdentifier, roleIdentifier.getId());
	}
}
