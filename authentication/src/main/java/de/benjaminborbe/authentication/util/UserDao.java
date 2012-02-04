package de.benjaminborbe.authentication.util;

import de.benjaminborbe.tools.dao.Dao;

public interface UserDao extends Dao<UserBean, String> {

	UserBean findByUsername(String username);

	UserBean findOrCreateByUsername(String username);

}
