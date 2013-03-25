package de.benjaminborbe.authentication.verifycredential;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;

public interface AuthenticationVerifyCredential {

	long getPriority();

	boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException;

	String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException;

	boolean existsUser(UserIdentifier userIdentifier) throws AuthenticationServiceException;

	boolean isActive();

	User getUser(UserIdentifier userIdentifier) throws AuthenticationServiceException;

}
