package de.benjaminborbe.authentication.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.util.SessionBean;
import de.benjaminborbe.authentication.util.SessionDao;
import de.benjaminborbe.authentication.util.UserBean;
import de.benjaminborbe.authentication.util.UserDao;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final SessionDao sessionDao;

	private final UserDao userDao;

	@Inject
	public AuthenticationServiceImpl(final Logger logger, final SessionDao sessionDao, final UserDao userDao) {
		this.logger = logger;
		this.sessionDao = sessionDao;
		this.userDao = userDao;
	}

	@Override
	public boolean verifyCredential(final String username, final String password) {
		logger.debug("execute");
		final UserBean user = userDao.findByUsername(username);
		return user != null && user.getPassword().equals(password);
	}

	@Override
	public boolean login(final SessionIdentifier sessionIdentifier, final String username, final String password) {
		if (verifyCredential(username, password)) {
			final SessionBean session = sessionDao.findOrCreateBySessionId(sessionIdentifier);
			session.setCurrentUser(username);
			sessionDao.save(session);
			return true;
		}
		return false;
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionIdentifier) {
		final SessionBean session = sessionDao.findBySessionId(sessionIdentifier);
		return session != null && session.getCurrentUser() != null;
	}

	@Override
	public boolean logout(final SessionIdentifier sessionIdentifier) {
		final SessionBean session = sessionDao.findBySessionId(sessionIdentifier);
		if (session != null && session.getCurrentUser() != null) {
			session.setCurrentUser(null);
			return true;
		}
		return false;
	}

	@Override
	public UserIdentifier getCurrentUser(final SessionIdentifier sessionIdentifier) {
		final SessionBean session = sessionDao.findBySessionId(sessionIdentifier);
		if (session != null) {
			return new UserIdentifier(session.getCurrentUser());
		}
		return null;
	}

	@Override
	public boolean register(final SessionIdentifier sessionIdentifier, final String username, final String password) {
		if (userDao.findByUsername(username) != null) {
			return false;
		}
		final UserBean user = userDao.findOrCreateByUsername(username);
		user.setPassword(password);
		userDao.save(user);
		return login(sessionIdentifier, username, password);
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionIdentifier) {
		final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
		if (userIdentifier == null) {
			return false;
		}
		final String username = userIdentifier.getId();
		if (username == null) {
			return false;
		}
		final UserBean user = userDao.findByUsername(username);
		if (user == null) {
			return false;
		}
		userDao.delete(user);
		return logout(sessionIdentifier);
	}

	@Override
	public boolean changePassword(final SessionIdentifier sessionIdentifier, final String currentPassword, final String newPassword) {
		final UserIdentifier userIdentifier = getCurrentUser(sessionIdentifier);
		if (userIdentifier == null) {
			return false;
		}
		final String username = userIdentifier.getId();
		if (username == null) {
			return false;
		}
		final UserBean user = userDao.findByUsername(username);
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

}
