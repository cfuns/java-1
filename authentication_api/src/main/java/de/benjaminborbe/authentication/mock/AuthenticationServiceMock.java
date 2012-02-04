package de.benjaminborbe.authentication.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	@Inject
	public AuthenticationServiceMock() {
	}

	@Override
	public boolean verifyCredential(final String username, final String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final String username, final String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCurrentUser(final SessionIdentifier sessionIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean register(final SessionIdentifier sessionIdentifier, final String username, final String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

}
