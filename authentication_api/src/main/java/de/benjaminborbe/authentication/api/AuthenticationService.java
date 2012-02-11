package de.benjaminborbe.authentication.api;

public interface AuthenticationService {

	boolean verifyCredential(String username, String password) throws AuthenticationServiceException;

	boolean login(SessionIdentifier sessionIdentifier, String username, String password) throws AuthenticationServiceException;

	boolean isLoggedIn(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean logout(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	UserIdentifier getCurrentUser(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean register(SessionIdentifier sessionIdentifier, String username, String password) throws AuthenticationServiceException;

	boolean unregister(SessionIdentifier sessionIdentifier) throws AuthenticationServiceException;

	boolean changePassword(SessionIdentifier sessionIdentifier, String currentPassword, String newPassword) throws AuthenticationServiceException;
}
