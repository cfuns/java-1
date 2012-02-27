package de.benjaminborbe.authorization.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;

public interface AuthorizationService {

	boolean hasRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean hasRole(UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	void expectRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException, PermissionDeniedException;

	boolean hasPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	void expectPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException, PermissionDeniedException;

	boolean createRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException, AuthorizationServiceException;

	boolean addUserRole(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException;

	boolean removeUserRole(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException;

	RoleIdentifier createRoleIdentifier(String rolename);

	Collection<RoleIdentifier> roleList() throws AuthorizationServiceException;
}
