package de.benjaminborbe.authentication.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.util.SessionBean;
import de.benjaminborbe.authentication.util.SessionDao;

@Singleton
public class AuthenticationServiceImpl implements AuthenticationService {

	private final Logger logger;

	private final String USERNAME = "bborbe";

	private final String PASSWORD = "test123";

	private final SessionDao sessionDao;

	@Inject
	public AuthenticationServiceImpl(final Logger logger, final SessionDao sessionDao) {
		this.logger = logger;
		this.sessionDao = sessionDao;
	}

	@Override
	public boolean verifyCredential(final String username, final String password) {
		logger.debug("execute");
		return USERNAME.equals(username) && PASSWORD.equals(password);
	}

	@Override
	public boolean login(final String sessionId, final String username, final String password) {
		if (verifyCredential(username, password)) {
			final SessionBean session = sessionDao.findOrCreateBySessionId(sessionId);
			session.setCurrentUser(username);
			sessionDao.save(session);
			return true;
		}
		return false;
	}

	@Override
	public boolean isLoggedIn(final String sessionId) {
		final SessionBean session = sessionDao.findBySessionId(sessionId);
		return session != null && session.getCurrentUser() != null;
	}

	@Override
	public boolean logout(final String sessionId) {
		final SessionBean session = sessionDao.findBySessionId(sessionId);
		if (session != null && session.getCurrentUser() != null) {
			session.setCurrentUser(null);
			return true;
		}
		return false;
	}

	@Override
	public String getCurrentUser(final String sessionId) {
		final SessionBean session = sessionDao.findBySessionId(sessionId);
		if (session != null) {
			return session.getCurrentUser();
		}
		return null;
	}

}
