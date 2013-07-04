package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.tools.mapper.MapperListBase;

import javax.inject.Inject;

public class MapperPokerCardIdentifierList extends MapperListBase<PokerCardIdentifier> {

	@Inject
	public MapperPokerCardIdentifierList(final MapperPokerCardIdentifier mapperPokerCardIdentifier) {
		super(mapperPokerCardIdentifier);
	}

}
