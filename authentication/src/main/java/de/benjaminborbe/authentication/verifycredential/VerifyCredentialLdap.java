package de.benjaminborbe.authentication.verifycredential;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.ldap.LdapConnector;
import de.benjaminborbe.authentication.ldap.LdapException;

@Singleton
public class VerifyCredentialLdap implements VerifyCredential {

	private final LdapConnector ldapConnector;

	private final Logger logger;

	@Inject
	public VerifyCredentialLdap(final Logger logger, final LdapConnector ldapConnector) {
		this.logger = logger;
		this.ldapConnector = ldapConnector;
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		try {
			return ldapConnector.verify(userIdentifier.getId(), password);
		}
		catch (final LdapException e) {
			logger.debug(e.getClass().getName(), e);
			throw new AuthenticationServiceException(e);
		}
	}

	@Override
	public String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			return ldapConnector.getFullname(userIdentifier.getId());
		}
		catch (final LdapException e) {
			logger.debug(e.getClass().getName(), e);
			throw new AuthenticationServiceException(e.getClass().getName(), e);
		}
	}

}
