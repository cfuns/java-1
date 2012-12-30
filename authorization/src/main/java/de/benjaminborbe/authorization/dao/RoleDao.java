package de.benjaminborbe.authorization.dao;

import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface RoleDao extends Dao<RoleBean, RoleIdentifier> {

	RoleBean findByRolename(final RoleIdentifier roleIdentifier) throws StorageException;

	RoleBean findOrCreateByRolename(final RoleIdentifier roleIdentifier) throws StorageException;
}
