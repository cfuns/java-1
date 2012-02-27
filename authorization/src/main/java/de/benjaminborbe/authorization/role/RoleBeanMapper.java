package de.benjaminborbe.authorization.role;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.role.RoleBean;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class RoleBeanMapper extends BaseMapper<RoleBean> {

	@Inject
	public RoleBeanMapper(final Provider<RoleBean> provider) {
		super(provider);
	}

	@Override
	public void map(final RoleBean object, final Map<String, String> data) throws MapException {
		data.put("id", toString(object.getId()));
	}

	private String toString(final RoleIdentifier id) {
		return id != null ? id.getId() : null;
	}

	@Override
	public void map(final Map<String, String> data, final RoleBean object) throws MapException {
		object.setId(toRoleIdentifier(data.get("id")));
	}

	private RoleIdentifier toRoleIdentifier(final String id) {
		return id != null ? new RoleIdentifier(id) : null;
	}

}
