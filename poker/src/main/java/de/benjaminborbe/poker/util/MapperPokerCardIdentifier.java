package de.benjaminborbe.poker.util;

import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.card.PokerCardIdentifierBuilder;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperPokerCardIdentifier implements Mapper<PokerCardIdentifier> {

	private final PokerCardIdentifierBuilder pokerCardIdentifierBuilder;

	@Inject
	public MapperPokerCardIdentifier(final PokerCardIdentifierBuilder pokerCardIdentifierBuilder) {
		this.pokerCardIdentifierBuilder = pokerCardIdentifierBuilder;
	}

	@Override
	public PokerCardIdentifier fromString(final String string) throws MapException {
		return pokerCardIdentifierBuilder.buildIdentifier(string);
	}

	@Override
	public String toString(final PokerCardIdentifier value) throws MapException {
		return value != null ? value.getId() : null;
	}

}
