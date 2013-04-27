package de.benjaminborbe.authentication.core.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserDaoCache extends DaoCache<UserBean, UserIdentifier> implements UserDao {

	@Inject
	public UserDaoCache(final Logger logger, final Provider<UserBean> provider) {
		super(logger, provider);
	}

	@Override
	public UserBean findOrCreate(final UserIdentifier userIdentifier) {
		{
			final UserBean user = load(userIdentifier);
			if (user != null) {
				return user;
			}
		}
		{
			final UserBean user = create();
			user.setId(userIdentifier);
			save(user);
			return user;
		}
	}

}
