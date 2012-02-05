package de.benjaminborbe.authentication.api;

public interface AuthenticationService {

	boolean verifyCredential(String username, String password);

	boolean login(SessionIdentifier sessionIdentifier, String username, String password);

	boolean isLoggedIn(SessionIdentifier sessionIdentifier);

	boolean logout(SessionIdentifier sessionIdentifier);

	UserIdentifier getCurrentUser(SessionIdentifier sessionIdentifier);

	boolean register(SessionIdentifier sessionIdentifier, String username, String password);

	boolean unregister(SessionIdentifier sessionIdentifier);

	boolean changePassword(SessionIdentifier sessionIdentifier, String currentPassword, String newPassword);
}
