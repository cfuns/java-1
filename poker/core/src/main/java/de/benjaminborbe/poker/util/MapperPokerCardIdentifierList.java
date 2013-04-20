package de.benjaminborbe.poker.util;

import javax.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.tools.mapper.MapperListBase;

public class MapperPokerCardIdentifierList extends MapperListBase<PokerCardIdentifier> {

	@Inject
	public MapperPokerCardIdentifierList(final MapperPokerCardIdentifier mapperPokerCardIdentifier) {
		super(mapperPokerCardIdentifier);
	}

}
