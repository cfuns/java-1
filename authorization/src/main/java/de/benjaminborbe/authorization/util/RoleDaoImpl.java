package de.benjaminborbe.authorization.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.tools.DaoCache;

@Singleton
public class RoleDaoImpl extends DaoCache<RoleBean, String> implements RoleDao {

	@Inject
	public RoleDaoImpl(final Logger logger, final Provider<RoleBean> provider) {
		super(logger, provider);

		init();
	}

	protected void init() {
		findOrCreateByRolename("admin");
	}

	@Override
	public RoleBean findByRolename(final String rolename) {
		for (final RoleBean session : getAll()) {
			if (session.getId().equals(rolename)) {
				return session;
			}
		}
		return null;
	}

	@Override
	public RoleBean findOrCreateByRolename(final String rolename) {
		{
			final RoleBean session = findByRolename(rolename);
			if (session != null) {
				return session;
			}
		}
		{
			final RoleBean session = create();
			session.setId(rolename);
			save(session);
			return session;
		}
	}
}
