package de.benjaminborbe.wow.account;

import de.benjaminborbe.tools.mapper.Mapper;

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
