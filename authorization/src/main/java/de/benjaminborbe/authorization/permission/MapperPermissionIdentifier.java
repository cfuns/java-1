package de.benjaminborbe.authorization.permission;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperPermissionIdentifier implements Mapper<PermissionIdentifier> {

	@Override
	public String toString(final PermissionIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public PermissionIdentifier fromString(final String value) {
		return value != null ? new PermissionIdentifier(value) : null;
	}

}
