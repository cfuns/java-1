package de.benjaminborbe.poker.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;

public interface PokerService {

	PokerGameIdentifier createGame(String name) throws PokerServiceException, ValidationException;

	Collection<PokerGameIdentifier> getGames() throws PokerServiceException;

	PokerGame getGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	void startGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException;

	PokerGameIdentifier createGameIdentifier(String id) throws PokerServiceException;

	Collection<PokerPlayerIdentifier> getPlayers(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayer getPlayer(PokerPlayerIdentifier playerIdentifier) throws PokerServiceException;

	Collection<PokerCardIdentifier> getCards(PokerPlayerIdentifier playerIdentifier) throws PokerServiceException;

	Collection<PokerCardIdentifier> getCards(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier createPlayer(String name) throws PokerServiceException, ValidationException;

	PokerPlayerIdentifier createPlayerIdentifier(String id) throws PokerServiceException;

	void joinGame(PokerGameIdentifier gameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void leaveGame(PokerGameIdentifier gameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

}
