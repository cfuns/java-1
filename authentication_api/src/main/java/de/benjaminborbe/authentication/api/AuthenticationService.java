package de.benjaminborbe.authentication.api;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

	boolean verifyCredential(UserIdentifier userIdentifier, String password) throws AuthenticationServiceException;

	boolean login(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, String password) throws AuthenticationServiceException;

	boolean isLoggedIn(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean logout(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	UserIdentifier getCurrentUser(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean register(SessionIdentifier sessionIdentifier, UserIdentifier userIdentifier, String email, String password) throws AuthenticationServiceException;

	boolean unregister(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean changePassword(SessionIdentifier sessionIdentifier, String currentPassword, String newPassword) throws AuthenticationServiceException;

	UserIdentifier createUserIdentifier(String username) throws AuthenticationServiceException;

	SessionIdentifier createSessionIdentifier(HttpServletRequest request) throws AuthenticationServiceException;
}
