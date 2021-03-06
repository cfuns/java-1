package de.benjaminborbe.authentication.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionDaoStorage extends DaoStorage<SessionBean, SessionIdentifier> implements SessionDao {

	private static final String COLUMNFAMILY = "session";

	private final Logger logger;

	@Inject
	public SessionDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<SessionBean> beanProvider,
		final SessionBeanMapper mapper,
		final SessionIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.logger = logger;
	}

	@Override
	public SessionBean findOrCreate(final SessionIdentifier sessionId) throws StorageException {
		logger.debug("findOrCreate - sessionId: " + sessionId);
		if (sessionId == null) {
			throw new StorageException("can't load or create session without sessionId");
		}
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
