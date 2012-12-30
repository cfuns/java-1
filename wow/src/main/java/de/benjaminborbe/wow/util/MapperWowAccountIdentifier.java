package de.benjaminborbe.wow.util;

import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.wow.dao.WowAccountIdentifier;

public class MapperWowAccountIdentifier implements Mapper<WowAccountIdentifier> {

	@Override
	public String toString(final WowAccountIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public WowAccountIdentifier fromString(final String value) {
		return value != null ? new WowAccountIdentifier(value) : null;
	}

}
