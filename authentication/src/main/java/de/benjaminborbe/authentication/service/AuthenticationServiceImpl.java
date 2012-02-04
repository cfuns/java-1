package de.benjaminborbe.authentication.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
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
	public boolean login(final SessionIdentifier sessionId, final String username, final String password) {
		if (verifyCredential(username, password)) {
			final SessionBean session = sessionDao.findOrCreateBySessionId(sessionId);
			session.setCurrentUser(username);
			sessionDao.save(session);
			return true;
		}
		return false;
	}

	@Override
	public boolean isLoggedIn(final SessionIdentifier sessionId) {
		final SessionBean session = sessionDao.findBySessionId(sessionId);
		return session != null && session.getCurrentUser() != null;
	}

	@Override
	public boolean logout(final SessionIdentifier sessionId) {
		final SessionBean session = sessionDao.findBySessionId(sessionId);
		if (session != null && session.getCurrentUser() != null) {
			session.setCurrentUser(null);
			return true;
		}
		return false;
	}

	@Override
	public String getCurrentUser(final SessionIdentifier sessionId) {
		final SessionBean session = sessionDao.findBySessionId(sessionId);
		if (session != null) {
			return session.getCurrentUser();
		}
		return null;
	}

	@Override
	public boolean register(final SessionIdentifier sessionId, final String username, final String password) {
		if (userDao.findByUsername(username) != null) {
			return false;
		}
		final UserBean user = userDao.findOrCreateByUsername(username);
		user.setPassword(password);
		userDao.save(user);
		return login(sessionId, username, password);
	}

	@Override
	public boolean unregister(final SessionIdentifier sessionId) {
		final String username = getCurrentUser(sessionId);
		if (username == null) {
			return false;
		}
		final UserBean user = userDao.findByUsername(username);
		if (user == null) {
			return false;
		}
		userDao.delete(user);
		return logout(sessionId);
	}

}
