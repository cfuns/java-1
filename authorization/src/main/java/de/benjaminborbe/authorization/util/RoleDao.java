package de.benjaminborbe.authorization.util;

import de.benjaminborbe.storage.tools.Dao;

public interface RoleDao extends Dao<RoleBean, String> {

	RoleBean findByRolename(final String rolename);

	RoleBean findOrCreateByRolename(final String rolename);
}
