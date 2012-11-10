package de.benjaminborbe.authentication.verifycredential;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;

public interface AuthenticationVerifyCredential {

	boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException;

	String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException;

}
