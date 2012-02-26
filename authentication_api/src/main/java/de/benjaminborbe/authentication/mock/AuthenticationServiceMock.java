package de.benjaminborbe.authentication.mock;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	@Inject
	public AuthenticationServiceMock() {
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return null;
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public boolean changePassword(final SessionIdentifier sessionIdentifier, final String currentPassword, final String newPassword) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public UserIdentifier createUserIdentifier(final String username) throws AuthenticationServiceException {
		return null;
	}

	@Override
	public SessionIdentifier createSessionIdentifier(final HttpServletRequest request) throws AuthenticationServiceException {
		return null;
	}

	@Override
	public boolean register(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String email, final String password) throws AuthenticationServiceException {
		return false;
	}
}
