package de.benjaminborbe.authorization.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface AuthorizationService {

	boolean hasRole(SessionIdentifier sessionIdentifier, String roleName);

	boolean hasRole(SessionIdentifier sessionIdentifier, RoleIdentifier role);
}
