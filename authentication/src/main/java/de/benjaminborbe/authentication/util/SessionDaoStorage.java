package de.benjaminborbe.authentication.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.StorageDao;

@Singleton
public class SessionDaoStorage extends StorageDao<SessionBean> implements SessionDao {

	private static final String COLUMNFAMILY = "session";

	@Inject
	public SessionDaoStorage(final Logger logger, final StorageService storageService, final Provider<SessionBean> beanProvider, final SessionBeanMapper mapper) {
		super(logger, storageService, beanProvider, mapper);
	}

	@Override
	public SessionBean findBySessionId(final SessionIdentifier sessionId) throws StorageException {
		for (final SessionBean session : getAll()) {
			if (session.getId().equals(sessionId.getId())) {
				return session;
			}
		}
		return null;
	}

	@Override
	public SessionBean findOrCreateBySessionId(final SessionIdentifier sessionId) throws StorageException {
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

	@Override
	protected String getColumnFamily() {
		return COLUMNFAMILY;
	}
}
