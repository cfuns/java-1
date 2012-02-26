package de.benjaminborbe.authorization.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface AuthorizationService {

	Collection<Role> getRoles() throws AuthorizationServiceException;

	boolean hasRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	void expectRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException, PermissionDeniedException;

	boolean hasPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	void expectPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException, PermissionDeniedException;

	boolean createRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException, AuthorizationServiceException;
}
