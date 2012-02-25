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
		data.put("id", object.getId().getId());
	}

	@Override
	public void map(final Map<String, String> data, final RoleBean object) throws MapException {
		object.setId(new RoleIdentifier(data.get("id")));
	}

}
