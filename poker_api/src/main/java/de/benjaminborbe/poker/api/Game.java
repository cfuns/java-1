package de.benjaminborbe.poker.api;

import java.util.Collection;

public interface Game {

	GameIdentifier getId();

	boolean isRunning();

	Collection<PlayerIdentifier> getPlayers();

	PlayerIdentifier getActivePlayer();
}
