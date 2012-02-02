package de.benjaminborbe.authentication.api;

public interface AuthenticationService {

	boolean verifyCredential(String username, String password);

}
