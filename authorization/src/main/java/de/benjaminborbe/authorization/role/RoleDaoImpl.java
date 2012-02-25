package de.benjaminborbe.authorization.role;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.StorageDao;

@Singleton
public class RoleDaoImpl extends StorageDao<RoleBean, RoleIdentifier> implements RoleDao {

	private static final String COLUMN_FAMILY = "role";

	@Inject
	public RoleDaoImpl(final Logger logger, final StorageService storageService, final Provider<RoleBean> beanProvider, final RoleBeanMapper mapper) {
		super(logger, storageService, beanProvider, mapper);
	}

	@Override
	public RoleBean findByRolename(final RoleIdentifier roleIdentifier) throws StorageException {
		for (final RoleBean role : getAll()) {
			if (role.getId().equals(roleIdentifier)) {
				return role;
			}
		}
		return null;
	}

	@Override
	public RoleBean findOrCreateByRolename(final RoleIdentifier roleIdentifier) throws StorageException {
		{
			final RoleBean session = findByRolename(roleIdentifier);
			if (session != null) {
				return session;
			}
		}
		{
			final RoleBean role = create();
			role.setId(roleIdentifier);
			save(role);
			return role;
		}
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}
}
