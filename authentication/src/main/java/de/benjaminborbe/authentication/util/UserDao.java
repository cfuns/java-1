package de.benjaminborbe.authentication.util;

import de.benjaminborbe.storage.tools.Dao;

public interface UserDao extends Dao<UserBean, String> {

	UserBean findByUsername(String username);

	UserBean findOrCreateByUsername(String username);

}
