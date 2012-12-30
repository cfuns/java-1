package de.benjaminborbe.dhl.status;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperDhlIdentifier implements Mapper<DhlIdentifier> {

	@Override
	public String toString(final DhlIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public DhlIdentifier fromString(final String value) {
		return value != null ? new DhlIdentifier(value) : null;
	}

}
