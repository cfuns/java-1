package de.benjaminborbe.authentication.verifycredential;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.user.UserBean;
import de.benjaminborbe.authentication.user.UserDao;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class VerifyCredentialStorage implements VerifyCredential {

	private final Logger logger;

	private final UserDao userDao;

	@Inject
	public VerifyCredentialStorage(final Logger logger, final UserDao userDao) {
		this.logger = logger;
		this.userDao = userDao;
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		try {
			logger.trace("verifyCredential");
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				logger.info("verifyCredential failed no user found with name " + userIdentifier);
				return false;
			}

			if (user.getPassword() != null && user.getPassword().equals(password)) {
				logger.trace("verifyCredential password match");
				return true;
			}
			else {
				logger.info("verifyCredential password missmatch");
				return false;
			}
		}
		catch (final StorageException e) {
			logger.debug(e.getClass().getSimpleName(), e);
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public String getFullname(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			final UserBean user = userDao.load(userIdentifier);
			if (user != null) {
				return user.getFullname();
			}
		}
		catch (final StorageException e) {
		}
		return "";
	}

}
