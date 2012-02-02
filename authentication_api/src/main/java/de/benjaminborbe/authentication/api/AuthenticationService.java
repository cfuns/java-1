package de.benjaminborbe.authentication.api;

public interface AuthenticationService {

	boolean verifyCredential(String username, String password);

	boolean login(String sessionId, String username, String password);

	boolean isLoggedIn(String sessionId);

	boolean logout(String sessionId);

	String getCurrentUser(String sessionId);
}
