package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperPokerGameIdentifier implements Mapper<PokerGameIdentifier> {

	@Override
	public String toString(final PokerGameIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public PokerGameIdentifier fromString(final String value) {
		return value != null ? new PokerGameIdentifier(value) : null;
	}

}
