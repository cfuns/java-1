package de.benjaminborbe.authorization.util;

import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperRoleIdentifier implements Mapper<RoleIdentifier> {

	@Override
	public String toString(final RoleIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public RoleIdentifier fromString(final String value) {
		return value != null ? new RoleIdentifier(value) : null;
	}

}
