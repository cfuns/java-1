package de.benjaminborbe.poker.util;

import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.tools.mapper.MapperListBase;

public class MapperPokerPlayerIdentifierList extends MapperListBase<PokerPlayerIdentifier> {

	@Inject
	public MapperPokerPlayerIdentifierList(final MapperPokerPlayerIdentifier mapperPokerPlayerIdentifier) {
		super(mapperPokerPlayerIdentifier);
	}

}
