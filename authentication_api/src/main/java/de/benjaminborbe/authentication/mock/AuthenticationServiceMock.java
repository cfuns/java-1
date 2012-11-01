package de.benjaminborbe.authentication.mock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	private final Map<UserIdentifier, String> userPassword = new HashMap<UserIdentifier, String>();

	private final Map<SessionIdentifier, UserIdentifier> sessionUser = new HashMap<SessionIdentifier, UserIdentifier>();

	@Inject
	public AuthenticationServiceMock() {
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		return userPassword.containsKey(userIdentifier) && userPassword.get(userIdentifier).equals(password);
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		if (verifyCredential(userIdentifier, password)) {
			sessionUser.put(sessionIdentifier, userIdentifier);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return sessionUser.containsKey(sessionIdentifier);
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		sessionUser.remove(sessionIdentifier);
		return true;
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return sessionUser.get(sessionIdentifier);
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
		return new UserIdentifier(username);
	}

	@Override
	public SessionIdentifier createSessionIdentifier(final HttpServletRequest request) throws AuthenticationServiceException {
		return new SessionIdentifier("test");
	}

	@Override
	public Collection<UserIdentifier> userList() throws AuthenticationServiceException {
		return null;
	}

	@Override
	public boolean existsUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public boolean existsSession(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public void expectLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		if (!isLoggedIn(sessionIdentifier))
			throw new LoginRequiredException("user not logged in");
	}

	@Override
	public boolean register(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String email, final String password, final String fullname)
			throws AuthenticationServiceException {
		userPassword.put(userIdentifier, password);
		return true;
	}

	@Override
	public String getFullname(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		return null;
	}
}
