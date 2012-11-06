package de.benjaminborbe.authentication.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.session.SessionBean;
import de.benjaminborbe.authentication.session.SessionDao;
import de.benjaminborbe.authentication.user.UserBean;
import de.benjaminborbe.authentication.user.UserDao;
import de.benjaminborbe.authentication.verifycredential.VerifyCredential;
import de.benjaminborbe.authentication.verifycredential.VerifyCredentialRegistry;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final SessionDao sessionDao;

	private final UserDao userDao;

	private final VerifyCredentialRegistry verifyCredentialRegistry;

	@Inject
	public AuthenticationServiceImpl(final Logger logger, final SessionDao sessionDao, final UserDao userDao, final VerifyCredentialRegistry verifyCredentialRegistry) {
		this.logger = logger;
		this.sessionDao = sessionDao;
		this.userDao = userDao;
		this.verifyCredentialRegistry = verifyCredentialRegistry;
	}

	@Override
	public boolean verifyCredential(final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		for (final VerifyCredential a : verifyCredentialRegistry.getAll()) {
			if (a.verifyCredential(userIdentifier, password)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String password) throws AuthenticationServiceException {
		try {
			if (verifyCredential(userIdentifier, password)) {
				final SessionBean session = sessionDao.findOrCreate(sessionIdentifier);
				session.setCurrentUser(userIdentifier);
				sessionDao.save(session);
				logger.trace("login success");
				return true;
			}
			else {
				logger.info("login failed");
				return false;
			}
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			return session != null && session.getCurrentUser() != null;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			if (session != null && session.getCurrentUser() != null) {
				session.setCurrentUser(null);
				sessionDao.save(session);
				return true;
			}
			return false;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final SessionBean session = sessionDao.load(sessionIdentifier);
			if (session != null) {
				return session.getCurrentUser();
			}
			return null;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean register(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier, final String email, final String password, final String fullname)
			throws AuthenticationServiceException {
		try {
			if (userDao.load(userIdentifier) != null) {
				logger.info("user " + userIdentifier + " already exists");
				return false;
			}
			final UserBean user = userDao.create();
			user.setId(userIdentifier);
			user.setEmail(email);
			user.setPassword(password);
			userDao.save(user);
			logger.info("registerd user " + userIdentifier);
			return login(sessionIdentifier, userIdentifier, password);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			userDao.delete(user);
			return logout(sessionIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean changePassword(final SessionIdentifier sessionIdentifier, final String currentPassword, final String newPassword) throws AuthenticationServiceException {
		try {
			final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
			if (userIdentifier == null) {
				return false;
			}
			final UserBean user = userDao.load(userIdentifier);
			if (user == null) {
				return false;
			}
			if (!user.getPassword().equals(currentPassword)) {
				return false;
			}
			user.setPassword(newPassword);
			userDao.save(user);
			return true;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public UserIdentifier createUserIdentifier(final String username) throws AuthenticationServiceException {
		return new UserIdentifier(username);
	}

	@Override
	public SessionIdentifier createSessionIdentifier(final HttpServletRequest request) throws AuthenticationServiceException {
		return new SessionIdentifier(request.getSession().getId());
	}

	@Override
	public Collection<UserIdentifier> userList() throws AuthenticationServiceException {
		try {
			final Set<UserIdentifier> result = new HashSet<UserIdentifier>();
			final IdentifierIterator<UserIdentifier> i = userDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final IdentifierIteratorException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean existsUser(final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		try {
			return userDao.exists(userIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public boolean existsSession(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException {
		try {
			return sessionDao.exists(sessionIdentifier);
		}
		catch (final StorageException e) {
			throw new AuthenticationServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void expectLoggedIn(final SessionIdentifier sessionIdentifier) throws AuthenticationServiceException, LoginRequiredException {
		if (!isLoggedIn(sessionIdentifier)) {
			throw new LoginRequiredException("user not logged in");
		}
	}

	@Override
	public String getFullname(final SessionIdentifier sessionIdentifier, final UserIdentifier userIdentifier) throws AuthenticationServiceException {
		for (final VerifyCredential a : verifyCredentialRegistry.getAll()) {
			final String username = a.getFullname(userIdentifier);
			if (username != null && username.length() > 0) {
				return username;
			}
		}
		return null;
	}
}
