package de.benjaminborbe.authentication.core.dao;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;
import org.slf4j.Logger;

@Singleton
public class SessionDaoCache extends DaoCache<SessionBean, SessionIdentifier> implements SessionDao {

	@Inject
	public SessionDaoCache(final Logger logger, final Provider<SessionBean> provider) {
		super(logger, provider);
	}

}
