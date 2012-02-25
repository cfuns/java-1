package de.benjaminborbe.authorization.permission;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class PermissionBeanMapper extends BaseMapper<PermissionBean> {

	@Inject
	public PermissionBeanMapper(final Provider<PermissionBean> provider) {
		super(provider);
	}

	@Override
	public void map(final PermissionBean object, final Map<String, String> data) throws MapException {
		data.put("id", object.getId() != null ? object.getId().getId() : null);
	}

	@Override
	public void map(final Map<String, String> data, final PermissionBean object) throws MapException {
		object.setId(data.get("id") != null ? new PermissionIdentifier(data.get("id")) : null);
	}

}
