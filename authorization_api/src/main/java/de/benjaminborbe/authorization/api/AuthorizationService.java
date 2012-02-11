package de.benjaminborbe.authorization.api;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface AuthorizationService {

	boolean hasRole(SessionIdentifier sessionIdentifier, String roleName) throws AuthenticationServiceException;

	boolean hasRole(SessionIdentifier sessionIdentifier, RoleIdentifier role) throws AuthenticationServiceException;
}
