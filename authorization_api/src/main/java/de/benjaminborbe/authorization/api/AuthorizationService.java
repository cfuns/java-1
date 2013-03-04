package de.benjaminborbe.authorization.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;

public interface AuthorizationService {

	String ROLE_ADMIN = "admin";

	String ROLE_LOGGED_IN = "loggedIn";

	String ROLE_LOGGED_OUT = "loggedOut";

	String PERMISSION_CREATE_ROLE = "createRole";

	boolean addPermissionRole(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException, LoginRequiredException;

	boolean addUserRole(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException, LoginRequiredException;

	PermissionIdentifier createPermissionIdentifier(String permissionName) throws AuthorizationServiceException;

	boolean createRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException, AuthorizationServiceException, ValidationException;

	RoleIdentifier createRoleIdentifier(String roleName) throws AuthorizationServiceException;

	boolean existsPermission(PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	boolean existsRole(RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	void expectAdminRole(SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException, PermissionDeniedException;

	void expectRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectUser(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	Collection<UserIdentifier> getUserWithRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean hasAdminRole(SessionIdentifier sessionIdentifier) throws AuthorizationServiceException;

	boolean hasPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	boolean hasRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean hasRole(UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	Collection<PermissionIdentifier> permissionList() throws AuthorizationServiceException;

	Collection<PermissionIdentifier> permissionList(RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean removePermissionRole(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException, LoginRequiredException;

	boolean removeUserRole(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
			AuthorizationServiceException, LoginRequiredException;

	Collection<RoleIdentifier> roleList() throws AuthorizationServiceException;

	void expectUser(UserIdentifier currentUser, UserIdentifier userIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectUser(SessionIdentifier sessionIdentifier, Collection<UserIdentifier> userIdentifiers) throws AuthorizationServiceException, PermissionDeniedException,
			LoginRequiredException;

	void expectUser(UserIdentifier currentUser, Collection<UserIdentifier> userIdentifiers) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void deleteRole(SessionIdentifier sessionIdentifier, RoleIdentifier roleIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectOneOfRoles(SessionIdentifier sessionIdentifier, RoleIdentifier... roleIdentifiers) throws AuthorizationServiceException, PermissionDeniedException,
			LoginRequiredException;

	boolean hasOneOfRoles(SessionIdentifier sessionIdentifier, RoleIdentifier... roleIdentifiers) throws AuthorizationServiceException;

	void expectOneOfPermissions(SessionIdentifier sessionIdentifier, PermissionIdentifier... permissionIdentifiers) throws AuthorizationServiceException, PermissionDeniedException;

	boolean hasOneOfPermissions(SessionIdentifier sessionIdentifier, PermissionIdentifier... permissionIdentifiers) throws AuthorizationServiceException;

}
