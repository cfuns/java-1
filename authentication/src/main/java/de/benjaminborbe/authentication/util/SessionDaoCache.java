package de.benjaminborbe.authentication.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;

@Singleton
public class SessionDaoCache extends DaoCache<SessionBean, String> implements SessionDao {

	@Inject
	public SessionDaoCache(final Logger logger, final Provider<SessionBean> provider) {
		super(logger, provider);
	}

	@Override
	public SessionBean findBySessionId(final SessionIdentifier sessionId) {
		for (final SessionBean session : getAll()) {
			if (session.getId().equals(sessionId.getId())) {
				return session;
			}
		}
		return null;
	}

	@Override
	public SessionBean findOrCreateBySessionId(final SessionIdentifier sessionId) {
		{
			final SessionBean session = findBySessionId(sessionId);
			if (session != null) {
				return session;
			}
		}
		{
			final SessionBean session = create();
			session.setId(sessionId.getId());
			save(session);
			return session;
		}
	}
}
