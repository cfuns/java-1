package de.benjaminborbe.authentication.user;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class UserDaoStorage extends DaoStorage<UserBean, UserIdentifier> implements UserDao {

	private static final String COLUMN_FAMILY = "user";

	@Inject
	public UserDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<UserBean> beanProvider,
			final UserBeanMapper mapper,
			final UserIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public UserBean findOrCreate(final UserIdentifier userIdentifier) throws StorageException {
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
