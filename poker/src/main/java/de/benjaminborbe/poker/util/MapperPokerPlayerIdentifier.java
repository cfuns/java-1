package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperPokerPlayerIdentifier implements Mapper<PokerPlayerIdentifier> {

	@Override
	public String toString(final PokerPlayerIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public PokerPlayerIdentifier fromString(final String value) {
		return value != null ? new PokerPlayerIdentifier(value) : null;
	}

}
