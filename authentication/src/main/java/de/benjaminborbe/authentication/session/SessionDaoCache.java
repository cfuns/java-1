package de.benjaminborbe.authentication.session;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;

@Singleton
public class SessionDaoCache extends DaoCache<SessionBean, SessionIdentifier> implements SessionDao {

	@Inject
	public SessionDaoCache(final Logger logger, final Provider<SessionBean> provider) {
		super(logger, provider);
	}

	@Override
	public SessionBean findOrCreate(final SessionIdentifier sessionId) {
		{
			final SessionBean session = load(sessionId);
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
