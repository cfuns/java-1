package de.benjaminborbe.authentication.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.DaoCache;

@Singleton
public class UserDaoImpl extends DaoCache<UserBean, String> implements UserDao {

	@Inject
	public UserDaoImpl(final Logger logger, final Provider<UserBean> provider) {
		super(logger, provider);

		init();
	}

	protected void init() {
		final UserBean user = findOrCreateByUsername("bborbe");
		user.setPassword("mazdazx");
		save(user);
	}

	@Override
	public UserBean findByUsername(final String username) {
		for (final UserBean session : getAll()) {
			if (session.getId().equals(username)) {
				return session;
			}
		}
		return null;
	}

	@Override
	public UserBean findOrCreateByUsername(final String username) {
		{
			final UserBean session = findByUsername(username);
			if (session != null) {
				return session;
			}
		}
		{
			final UserBean session = create();
			session.setId(username);
			save(session);
			return session;
		}
	}
}
