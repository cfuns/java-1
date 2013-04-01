package de.benjaminborbe.authentication.core.verifycredential;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.User;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.core.dao.UserBean;
import de.benjaminborbe.authentication.core.dao.UserDao;
import de.benjaminborbe.authentication.core.util.AuthenticationPasswordEncryptionService;
import de.benjaminborbe.storage.api.StorageException;
import org.slf4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Singleton
public class AuthenticationVerifyCredentialStorage implements AuthenticationVerifyCredential {

	private final Logger logger;

	private final UserDao userDao;

	private final AuthenticationPasswordEncryptionService passwordEncryptionService;

	@Inject
	public AuthenticationVerifyCredentialStorage(final Logger logger, final UserDao userDao, final AuthenticationPasswordEncryptionService passwordEncryptionService) {
		this.logger = logger;
		this.userDao = userDao;
		this.passwordEncryptionService = passwordEncryptionService;
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		try {
			logger.trace("verifyCredential");

			if (userIdentifier.getId() == null || userIdentifier.getId().length() == 0) {
				logger.info("verifyCredential failed no user found with name " + userIdentifier);
				return false;
			}

			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				logger.info("verifyCredential failed no user found with name " + userIdentifier);
				return false;
			}

			if (user.getPassword() == null) {
				logger.info("verifyCredential failed password is null for user " + userIdentifier);
				return false;
			}

			if (user.getPasswordSalt() == null) {
				logger.info("verifyCredential failed passwordSalt is null for user " + userIdentifier);
				return false;
			}

			if (passwordEncryptionService.authenticate(password, user.getPassword(), user.getPasswordSalt())) {
				logger.trace("verifyCredential password match");
				return true;
			} else {
				logger.info("verifyCredential password missmatch");
				return false;
			}
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (final NoSuchAlgorithmException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		} catch (final InvalidKeySpecException e) {
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
		} catch (final StorageException e) {
		}
		return "";
	}

	@Override
	public boolean existsUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			return userDao.exists(userIdentifier);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public User getUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			return userDao.load(userIdentifier);
		} catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public long getPriority() {
		return 2;
	}
}
