package de.benjaminborbe.authentication.mock;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserDto;
import de.benjaminborbe.authentication.api.UserIdentifier;
import org.easymock.EasyMock;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	public static final String SESSION_ID = "sid";

	public static final String ADMIN_USERNAME = "testAdmin";

	public static final String ADMIN_PASSWORD = "Test123!";

	public static final String USER_USERNAME = "testUser";

	public static final String USER_PASSWORD = "Test123!";

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
	public boolean login(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier,
		final String password
	) throws AuthenticationServiceException {
		if (verifyCredential(userIdentifier, password)) {
			sessionUser.put(sessionIdentifier, userIdentifier);
			return true;
		} else {
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
	public UserIdentifier createUserIdentifier(final String username) throws AuthenticationServiceException {
		return new UserIdentifier(username);
	}

	@Override
	public SessionIdentifier createSessionIdentifier(final HttpServletRequest request) throws AuthenticationServiceException {
		return new SessionIdentifier("test");
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
	public UserIdentifier register(
		final SessionIdentifier sessionIdentifier, final String shortenUrl, final String validateEmailBaseUrl, final String username, final String email,
		final String password
	) throws AuthenticationServiceException {
		final UserIdentifier userIdentifier = createUserIdentifier(username);
		userPassword.put(userIdentifier, password);
		login(sessionIdentifier, userIdentifier, password);
		return userIdentifier;
	}

	@Override
	public String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		return null;
	}

	@Override
	public Collection<UserIdentifier> userList(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return null;
	}

	@Override
	public void switchUser(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws AuthenticationServiceException, LoginRequiredException {
	}

	@Override
	public boolean isSuperAdmin(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public boolean isSuperAdmin(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public TimeZone getTimeZone(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		return TimeZone.getDefault();
	}

	@Override
	public boolean login(
		final SessionIdentifier sessionIdentifier,
		final String username,
		final String password
	) throws AuthenticationServiceException, ValidationException {
		return login(sessionIdentifier, new UserIdentifier(username), password);
	}

	@Override
	public void expectSuperAdmin(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {

	}

	@Override
	public boolean changePassword(
		final SessionIdentifier sessionIdentifier,
		final String currentPassword,
		final String newPassword,
		final String newPasswordRepeat
	)
		throws AuthenticationServiceException, LoginRequiredException, ValidationException {
		return false;
	}

	@Override
	public boolean verifyEmailToken(final UserIdentifier userIdentifier, final String token) throws AuthenticationServiceException {
		return false;
	}

	@Override
	public void updateUser(
		final SessionIdentifier sessionIdentifier, final String shortenUrl, final String validateEmailBaseUrl, final String email, final String fullname,
		final String timeZone
	) throws AuthenticationServiceException, LoginRequiredException, ValidationException {
	}

	@Override
	public UserIdentifier createUser(
		final SessionIdentifier sessionId,
		final UserDto userDto
	) throws AuthenticationServiceException, LoginRequiredException, ValidationException {
		return null;
	}

	@Override
	public void deleteUser(
		final SessionIdentifier sessionIdentifier,
		final UserIdentifier userIdentifier
	) throws AuthenticationServiceException, LoginRequiredException {
	}

	@Override
	public void sendPasswordLostEmail(
		final String shortenUrl,
		final String resetUrl,
		final UserIdentifier userIdentifier,
		final String email
	) throws AuthenticationServiceException,
		ValidationException {
	}

	@Override
	public void setNewPassword(final UserIdentifier userIdentifier, final String token, final String newPassword, final String newPasswordRepeat)
		throws AuthenticationServiceException, ValidationException {
	}

	@Override
	public User getUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		return null;
	}

	@Override
	public SessionIdentifier createSystemUser(final String pokerServer) throws AuthenticationServiceException {
		return new SessionIdentifier(pokerServer);
	}

	public SessionIdentifier mockSessionIdentifier() throws AuthenticationServiceException {
		final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
		EasyMock.replay(request);
		return createSessionIdentifier(request);
	}

	public UserIdentifier mockLoginUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		final String username = USER_USERNAME;
		final String password = USER_PASSWORD;
		final String shortenUrl = null;
		final String validateEmailBaseUrl = null;
		final String email = null;
		return register(sessionIdentifier, shortenUrl, validateEmailBaseUrl, username, email, password);
	}
}
