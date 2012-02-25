package de.benjaminborbe.authorization.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface AuthorizationService {

	Collection<Role> getRoles() throws AuthenticationServiceException;

	boolean hasRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthenticationServiceException;

	void expectRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthenticationServiceException, PermissionDeniedException;

	boolean hasPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthenticationServiceException;

	void expectPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthenticationServiceException, PermissionDeniedException;
}
