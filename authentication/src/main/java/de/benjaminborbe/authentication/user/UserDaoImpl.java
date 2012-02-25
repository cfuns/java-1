package de.benjaminborbe.authentication.user;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.DaoCache;

@Singleton
public class UserDaoImpl extends DaoCache<UserBean, UserIdentifier> implements UserDao {

	@Inject
	public UserDaoImpl(final Logger logger, final Provider<UserBean> provider) {
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
