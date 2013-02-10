package de.benjaminborbe.poker.player;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.IdentifierIterator;

public interface PokerPlayerDao extends Dao<PokerPlayerBean, PokerPlayerIdentifier> {

	IdentifierIterator<PokerPlayerIdentifier> getIdentifierIterator(PokerGameIdentifier gameIdentifier) throws StorageException;

}
