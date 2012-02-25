package de.benjaminborbe.authorization.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.Role;
import de.benjaminborbe.authorization.api.RoleIdentifier;

@Singleton
public class AuthorizationServiceMock implements AuthorizationService {

	@Inject
	public AuthorizationServiceMock() {
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier role) {
		return false;
	}

	@Override
	public boolean hasPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public void expectRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier roleIdentifier) throws AuthenticationServiceException, PermissionDeniedException {
	}

	@Override
	public void expectPermission(final SessionIdentifier sessionIdentifier, final PermissionIdentifier permissionIdentifier) throws AuthenticationServiceException,
			PermissionDeniedException {
	}

	@Override
	public Collection<Role> getRoles() throws AuthenticationServiceException {
		return null;
	}

}
