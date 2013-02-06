package de.benjaminborbe.poker.api;

import java.util.Collection;

public interface PokerService {

	Collection<GameIdentifier> getGames() throws PokerServiceException;

	Game getGame(GameIdentifier gameIdentifier) throws PokerServiceException;

	Collection<PlayerIdentifier> getPlayers(GameIdentifier gameIdentifier) throws PokerServiceException;

	Player getPlayer(PlayerIdentifier playerIdentifier) throws PokerServiceException;

	GameIdentifier createGame() throws PokerServiceException;

	void startGame(GameIdentifier gameIdentifier) throws PokerServiceException;
}
