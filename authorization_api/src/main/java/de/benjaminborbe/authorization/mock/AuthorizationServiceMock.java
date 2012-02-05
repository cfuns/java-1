package de.benjaminborbe.authorization.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.RoleIdentifier;

@Singleton
public class AuthorizationServiceMock implements AuthorizationService {

	@Inject
	public AuthorizationServiceMock() {
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final String roleName) {
		return false;
	}

	@Override
	public boolean hasRole(final SessionIdentifier sessionIdentifier, final RoleIdentifier role) {
		return false;
	}

}
