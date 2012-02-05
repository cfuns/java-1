package de.benjaminborbe.authorization.util;

import de.benjaminborbe.tools.dao.Dao;

public interface RoleDao extends Dao<RoleBean, String> {

	RoleBean findByRolename(final String rolename);

	RoleBean findOrCreateByRolename(final String rolename);
}
