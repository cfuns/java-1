package de.benjaminborbe.poker.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;

public interface PokerService {

	void deleteGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerGameIdentifier createGame(String name, long blind) throws PokerServiceException, ValidationException;

	Collection<PokerGameIdentifier> getGameIdentifiers() throws PokerServiceException;

	PokerGame getGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	void startGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException;

	PokerGameIdentifier createGameIdentifier(String id) throws PokerServiceException;

	Collection<PokerPlayerIdentifier> getPlayers(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayer getPlayer(PokerPlayerIdentifier playerIdentifier) throws PokerServiceException;

	Collection<PokerCardIdentifier> getHandCards(PokerPlayerIdentifier playerIdentifier) throws PokerServiceException;

	Collection<PokerCardIdentifier> getBoardCards(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier createPlayer(String name) throws PokerServiceException, ValidationException;

	PokerPlayerIdentifier createPlayerIdentifier(String id) throws PokerServiceException;

	void joinGame(PokerGameIdentifier gameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void leaveGame(PokerGameIdentifier gameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void nextRound(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier getActivePlayer(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	void fold(PokerGameIdentifier pokerGameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void call(PokerGameIdentifier pokerGameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void raise(PokerGameIdentifier pokerGameIdentifier, PokerPlayerIdentifier playerIdentifier, long amount) throws PokerServiceException, ValidationException;

	Collection<PokerGame> getGames() throws PokerServiceException;

	Collection<PokerPlayer> getPlayers() throws PokerServiceException;

	void deletePlayer(PokerPlayerIdentifier pokerPlayerIdentifier) throws PokerServiceException, ValidationException;

}
