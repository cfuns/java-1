package de.benjaminborbe.authentication.core.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface UserDao extends Dao<UserBean, UserIdentifier> {

	UserBean findOrCreate(UserIdentifier userIdentifier) throws StorageException;

}
