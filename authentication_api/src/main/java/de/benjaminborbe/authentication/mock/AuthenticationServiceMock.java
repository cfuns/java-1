package de.benjaminborbe.authentication.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	@Inject
	public AuthenticationServiceMock() {
	}

	@Override
	public boolean verifyCredential(final String username, final String password) {
		return true;
	}

	@Override
	public boolean login(final String sessionId, final String username, final String password) {
		return false;
	}

	@Override
	public boolean isLoggedIn(final String sessionId) {
		return false;
	}

	@Override
	public boolean logout(final String sessionId) {
		return false;
	}

	@Override
	public String getCurrentUser(final String sessionId) {
		return null;
	}

	@Override
	public boolean register(final String sessionId, final String username, final String password) {
		return false;
	}

	@Override
	public boolean unregister(final String sessionId) {
		return false;
	}
}
