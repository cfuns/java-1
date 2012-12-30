package de.benjaminborbe.authentication.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperSessionIdentifier implements Mapper<SessionIdentifier> {

	@Override
	public String toString(final SessionIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public SessionIdentifier fromString(final String value) {
		return value != null ? new SessionIdentifier(value) : null;
	}

}
