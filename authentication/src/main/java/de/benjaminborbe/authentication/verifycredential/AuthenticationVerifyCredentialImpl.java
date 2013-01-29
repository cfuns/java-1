package de.benjaminborbe.authentication.verifycredential;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;

public class AuthenticationVerifyCredentialImpl implements AuthenticationVerifyCredential {

	private final AuthenticationVerifyCredentialRegistry authenticationVerifyCredentialRegistry;

	private final Logger logger;

	@Inject
	public AuthenticationVerifyCredentialImpl(final Logger logger, final AuthenticationVerifyCredentialRegistry authenticationVerifyCredentialRegistry) {
		this.logger = logger;
		this.authenticationVerifyCredentialRegistry = authenticationVerifyCredentialRegistry;
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		logger.debug("try verifyCredential for user: " + userIdentifier);

		for (final AuthenticationVerifyCredential a : authenticationVerifyCredentialRegistry.getAll()) {
			if (a.isActive()) {
				try {
					if (a.verifyCredential(userIdentifier, password)) {
						return true;
					}
				}
				catch (final AuthenticationServiceException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
		return false;
	}

	@Override
	public String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		for (final AuthenticationVerifyCredential a : authenticationVerifyCredentialRegistry.getAll()) {
			if (a.isActive()) {
				final String username = a.getFullname(userIdentifier);
				if (username != null && username.length() > 0) {
					return username;
				}
			}
		}
		return null;
	}

	@Override
	public boolean existsUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		logger.debug("existsUser - user: " + userIdentifier);
		for (final AuthenticationVerifyCredential a : authenticationVerifyCredentialRegistry.getAll()) {
			if (a.isActive()) {
				logger.debug("existsUser - user: " + userIdentifier + " in " + a.getClass().getSimpleName());
				try {
					if (a.existsUser(userIdentifier)) {
						return true;
					}
				}
				catch (final AuthenticationServiceException e) {
					logger.warn(e.getClass().getName(), e);
				}
			}
		}
		logger.debug("existsUser - user not found: " + userIdentifier);
		return false;
	}

	@Override
	public boolean isActive() {
		return true;
	}

}
