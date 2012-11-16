package de.benjaminborbe.authentication.session;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class SessionDaoStorage extends DaoStorage<SessionBean, SessionIdentifier> implements SessionDao {

	private static final String COLUMNFAMILY = "session";

	@Inject
	public SessionDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<SessionBean> beanProvider,
			final SessionBeanMapper mapper,
			final SessionIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	public SessionBean findOrCreate(final SessionIdentifier sessionId) throws StorageException {
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

	@Override
	protected String getColumnFamily() {
		return COLUMNFAMILY;
	}
}
