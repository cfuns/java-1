package de.benjaminborbe.authentication.user;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Dao;

public interface UserDao extends Dao<UserBean, UserIdentifier> {

	UserBean findOrCreate(UserIdentifier userIdentifier);

}
