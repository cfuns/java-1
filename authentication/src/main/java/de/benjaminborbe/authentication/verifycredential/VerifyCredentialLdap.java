package de.benjaminborbe.authentication.verifycredential;

import javax.naming.NamingException;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.ldap.LdapConnector;

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
		return ldapConnector.verify(userIdentifier.getId(), password);
	}

	@Override
	public String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			return ldapConnector.getFullname(userIdentifier.getId());
		}
		catch (final NamingException e) {
			logger.debug(e.getClass().getName(), e);
			throw new AuthenticationServiceException(e.getClass().getName(), e);
		}
	}

}
