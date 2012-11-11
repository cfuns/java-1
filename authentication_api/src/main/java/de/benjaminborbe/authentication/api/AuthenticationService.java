package de.benjaminborbe.authentication.api;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

	boolean verifyCredential(UserIdentifier userIdentifier, String password) throws AuthenticationServiceException;

	boolean login(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, String password) throws AuthenticationServiceException;

	boolean isLoggedIn(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	void expectLoggedIn(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException;

	boolean logout(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	UserIdentifier getCurrentUser(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	String getFullname(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier) throws AuthenticationServiceException;

	boolean register(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, String email, String password, String fullname) throws AuthenticationServiceException;

	boolean unregister(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean changePassword(SessionIdentifier sessionIdentifier, String currentPassword, String newPassword) throws AuthenticationServiceException;

	UserIdentifier createUserIdentifier(String username) throws AuthenticationServiceException;

	SessionIdentifier createSessionIdentifier(HttpServletRequest request) throws AuthenticationServiceException;

	Collection<UserIdentifier> userList(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException;

	boolean existsUser(UserIdentifier userIdentifier) throws AuthenticationServiceException;

	boolean existsSession(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	User getUser(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier) throws AuthenticationServiceException, LoginRequiredException;

	void switchUser(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier) throws AuthenticationServiceException, LoginRequiredException, SuperAdminRequiredException;

	boolean isSuperAdmin(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean isSuperAdmin(UserIdentifier userIdentifier) throws AuthenticationServiceException;
}
