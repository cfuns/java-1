package de.benjaminborbe.authorization.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
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

	RoleIdentifier createRoleIdentifier(String roleName);

	Collection<RoleIdentifier> roleList() throws AuthorizationServiceException;

	PermissionIdentifier createPermissionIdentifier(String permissionName);

	Collection<PermissionIdentifier> permissionList() throws AuthorizationServiceException;

	Collection<PermissionIdentifier> permissionList(RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean removePermissionRole(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException;

	boolean addPermissionRole(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException;

	boolean existsRole(RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean existsPermission(PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	void expectAdminRole(SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, PermissionDeniedException;

	void expectUser(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	boolean hasAdminRole(SessionIdentifier sessionIdentifier) throws AuthorizationServiceException;

	Collection<UserIdentifier> getUserWithRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

}
