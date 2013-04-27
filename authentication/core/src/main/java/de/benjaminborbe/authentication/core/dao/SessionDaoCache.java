package de.benjaminborbe.authentication.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionDaoCache extends DaoCache<SessionBean, SessionIdentifier> implements SessionDao {

	@Inject
	public SessionDaoCache(final Logger logger, final Provider<SessionBean> provider) {
		super(logger, provider);
	}

}
