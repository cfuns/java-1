package de.benjaminborbe.authentication.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.DaoCache;

@Singleton
public class SessionDaoImpl extends DaoCache<SessionBean, String> implements SessionDao {

	@Inject
	public SessionDaoImpl(final Logger logger, final Provider<SessionBean> provider) {
		super(logger, provider);
	}

	@Override
	public SessionBean findBySessionId(final String sessionId) {
		for (final SessionBean session : getAll()) {
			if (session.getId().equals(sessionId)) {
				return session;
			}
		}
		return null;
	}

	@Override
	public SessionBean findOrCreateBySessionId(final String sessionId) {
		{
			final SessionBean session = findBySessionId(sessionId);
			if (session != null) {
				return session;
			}
		}
		{
			final SessionBean session = create();
			session.setId(sessionId);
			save(session);
			return session;
		}
	}
}
