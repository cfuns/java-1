package de.benjaminborbe.test.osgi;

import java.util.TimeZone;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;

public class TestUtil {

	public static final String EMAIL = "test@example.com";

	public static final String VALIDATEEMAILBASEURL = "http://example.com/test";

	public static final String SHORTENURL = "http://bb/bb/s";

	public static final String FULLNAME = "";

	public static final String LOGIN_USER = "testuser";

	public static final String LOGIN_ADMIN = "testadmin";

	public static final String PASSWORD = "Test123!";

	public static final String SESSION_ID = "sid";

	private final AuthenticationService authenticationService;

	private final StorageService storageService;

	public TestUtil(final AuthenticationService authenticationService, final StorageService storageService) {
		this.authenticationService = authenticationService;
		this.storageService = storageService;
	}

	public UserIdentifier createUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, ValidationException {
		final UserIdentifier userIdentifier = new UserIdentifier(LOGIN_USER);

		if (authenticationService.verifyCredential(sessionIdentifier, userIdentifier, PASSWORD)) {
			authenticationService.login(sessionIdentifier, userIdentifier, PASSWORD);
			authenticationService.unregister(sessionIdentifier);
		}

		if (authenticationService.isLoggedIn(sessionIdentifier)) {
			authenticationService.logout(sessionIdentifier);
		}

		authenticationService.register(sessionIdentifier, SHORTENURL, VALIDATEEMAILBASEURL, LOGIN_USER, EMAIL, PASSWORD, FULLNAME, TimeZone.getDefault());
		return userIdentifier;
	}

	public UserIdentifier createSuperAdmin(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, ValidationException, StorageException {
		final UserIdentifier userIdentifier = new UserIdentifier(LOGIN_ADMIN);

		if (authenticationService.verifyCredential(sessionIdentifier, userIdentifier, PASSWORD)) {
			authenticationService.login(sessionIdentifier, userIdentifier, PASSWORD);
			authenticationService.unregister(sessionIdentifier);
		}

		if (authenticationService.isLoggedIn(sessionIdentifier)) {
			authenticationService.logout(sessionIdentifier);
		}

		authenticationService.register(sessionIdentifier, SHORTENURL, VALIDATEEMAILBASEURL, LOGIN_ADMIN, EMAIL, PASSWORD, FULLNAME, TimeZone.getDefault());

		storageService.set("user", new StorageValue(LOGIN_ADMIN, storageService.getEncoding()), new StorageValue("superAdmin", storageService.getEncoding()), new StorageValue("true",
				storageService.getEncoding()));

		return userIdentifier;
	}

	public SessionIdentifier createSessionIdentifier() {
		return new SessionIdentifier(SESSION_ID);
	}
}
