package de.benjaminborbe.authentication.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	@Inject
	public AuthenticationServiceMock() {
	}

	@Override
	public boolean verifyCredential(final String username, final String password) {
		return false;
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final String username, final String password) {
		return false;
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) {
		return false;
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) {
		return false;
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) {
		return null;
	}

	@Override
	public boolean register(final SessionIdentifier sessionIdentifier, final String username, final String password) {
		return false;
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) {
		return false;
	}

	@Override
	public boolean changePassword(final SessionIdentifier sessionIdentifier, final String currentPassword, final String newPassword) {
		return false;
	}

}
