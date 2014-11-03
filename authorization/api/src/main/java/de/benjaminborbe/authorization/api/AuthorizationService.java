package de.benjaminborbe.authorization.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;

import java.util.Collection;

public interface AuthorizationService {

	String ROLE_ADMIN = "admin";

	String ROLE_LOGGED_IN = "loggedIn";

	String ROLE_LOGGED_OUT = "loggedOut";

	String PERMISSION_CREATE_ROLE = "createRole";

	RoleIdentifier getAdminRoleIdentifier() throws AuthorizationServiceException;

	RoleIdentifier getLoggedInRoleIdentifier() throws AuthorizationServiceException;

	RoleIdentifier getLoggedOutIdentifier() throws AuthorizationServiceException;

	boolean addPermissionRole(
		SessionIdentifier sessionIdentifier,
		PermissionIdentifier permissionIdentifier,
		RoleIdentifier roleIdentifier
	) throws PermissionDeniedException,
		AuthorizationServiceException, LoginRequiredException;

	boolean addUserRole(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
		AuthorizationServiceException, LoginRequiredException;

	PermissionIdentifier createPermissionIdentifier(String permissionName) throws AuthorizationServiceException;

	boolean createRole(
		SessionIdentifier sessionIdentifier,
		RoleIdentifier roleIdentifier
	) throws PermissionDeniedException, AuthorizationServiceException, ValidationException;

	RoleIdentifier createRoleIdentifier(String roleName) throws AuthorizationServiceException;

	boolean existsPermission(PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	void expectAdminRole(SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectPermission(
		SessionIdentifier sessionIdentifier,
		PermissionIdentifier permissionIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException;

	void expectUser(
		SessionIdentifier sessionIdentifier,
		UserIdentifier userIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	Collection<UserIdentifier> getUsersWithRole(RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean hasAdminRole(SessionIdentifier sessionIdentifier) throws AuthorizationServiceException;

	boolean hasPermission(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException;

	Collection<PermissionIdentifier> getPermissions() throws AuthorizationServiceException;

	Collection<PermissionIdentifier> getPermissions(RoleIdentifier roleIdentifier) throws AuthorizationServiceException;

	boolean removePermissionRole(
		SessionIdentifier sessionIdentifier,
		PermissionIdentifier permissionIdentifier,
		RoleIdentifier roleIdentifier
	) throws PermissionDeniedException,
		AuthorizationServiceException, LoginRequiredException;

	boolean removeUserRole(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, RoleIdentifier roleIdentifier) throws PermissionDeniedException,
		AuthorizationServiceException, LoginRequiredException;

	Collection<RoleIdentifier> getRoles() throws AuthorizationServiceException;

	void expectUser(
		UserIdentifier currentUser,
		UserIdentifier userIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectUser(
		SessionIdentifier sessionIdentifier,
		Collection<UserIdentifier> userIdentifiers
	) throws AuthorizationServiceException, PermissionDeniedException,
		LoginRequiredException;

	void expectUser(
		UserIdentifier currentUser,
		Collection<UserIdentifier> userIdentifiers
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void deleteRole(
		SessionIdentifier sessionIdentifier,
		RoleIdentifier roleIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException, LoginRequiredException;

	void expectOneOfPermissions(
		SessionIdentifier sessionIdentifier,
		PermissionIdentifier... permissionIdentifiers
	) throws AuthorizationServiceException, PermissionDeniedException;

	boolean hasOneOfPermissions(SessionIdentifier sessionIdentifier, PermissionIdentifier... permissionIdentifiers) throws AuthorizationServiceException;

	Collection<RoleIdentifier> getRoles(
		SessionIdentifier sessionIdentifier,
		UserIdentifier userIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException,
		LoginRequiredException;

	Collection<RoleIdentifier> getRoles(SessionIdentifier sessionIdentifier, PermissionIdentifier permissionIdentifier) throws AuthorizationServiceException,
		PermissionDeniedException, LoginRequiredException;

	void deletePermission(
		SessionIdentifier sessionIdentifier,
		PermissionIdentifier permissionIdentifier
	) throws AuthorizationServiceException, PermissionDeniedException,
		LoginRequiredException;

}
