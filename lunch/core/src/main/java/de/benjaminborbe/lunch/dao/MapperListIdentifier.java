package de.benjaminborbe.lunch.dao;

import de.benjaminborbe.tools.mapper.Mapper;

public class MapperListIdentifier implements Mapper<LunchUserSettingsIdentifier> {

	@Override
	public String toString(final LunchUserSettingsIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public LunchUserSettingsIdentifier fromString(final String value) {
		return value != null ? new LunchUserSettingsIdentifier(value) : null;
	}

}
