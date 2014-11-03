package de.benjaminborbe.poker.game;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.tools.util.IdGeneratorUUID;

import javax.inject.Inject;

public class PokerGameIdentifierGenerator {

	private final IdGeneratorUUID idGeneratorUUID;

	@Inject
	public PokerGameIdentifierGenerator(final IdGeneratorUUID idGeneratorUUID) {
		this.idGeneratorUUID = idGeneratorUUID;
	}

	public PokerGameIdentifier nextId() {
		return new PokerGameIdentifier(idGeneratorUUID.nextId());
	}
}
