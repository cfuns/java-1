package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.tools.mapper.MapperListBase;

import javax.inject.Inject;

public class MapperPokerPlayerIdentifierList extends MapperListBase<PokerPlayerIdentifier> {

	@Inject
	public MapperPokerPlayerIdentifierList(final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier) {
		super(mapperPokerPlayerIdentifier);
	}

}
