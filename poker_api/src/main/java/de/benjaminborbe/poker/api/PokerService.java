package de.benjaminborbe.poker.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface PokerService {

	String PERMISSION_ADMIN = "PokerAdmin";

	String PERMISSION_PLAYER = "PokerPlayer";

	void deleteGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException;

	PokerGameIdentifier createGame(PokerGameDto pokerGameDto) throws PokerServiceException, ValidationException;

	Collection<PokerGameIdentifier> getGameIdentifiers() throws PokerServiceException;

	PokerGame getGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	void startGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException;

	PokerGameIdentifier createGameIdentifier(String id) throws PokerServiceException;

	Collection<PokerPlayerIdentifier> getPlayers(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayer getPlayer(PokerPlayerIdentifier playerIdentifier) throws PokerServiceException;

	Collection<PokerCardIdentifier> getHandCards(PokerPlayerIdentifier playerIdentifier) throws PokerServiceException;

	Collection<PokerCardIdentifier> getBoardCards(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier createPlayer(PokerPlayerDto playerDto) throws PokerServiceException, ValidationException;

	void updatePlayer(PokerPlayerDto playerDto) throws PokerServiceException, ValidationException;

	PokerPlayerIdentifier createPlayerIdentifier(String id) throws PokerServiceException;

	void joinGame(PokerGameIdentifier gameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void leaveGame(PokerGameIdentifier gameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	PokerPlayerIdentifier getActivePlayer(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	void fold(PokerGameIdentifier pokerGameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void call(PokerGameIdentifier pokerGameIdentifier, PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException;

	void raise(PokerGameIdentifier pokerGameIdentifier, PokerPlayerIdentifier playerIdentifier, long amount) throws PokerServiceException, ValidationException;

	Collection<PokerGame> getGames() throws PokerServiceException;

	Collection<PokerPlayer> getPlayers() throws PokerServiceException;

	void deletePlayer(PokerPlayerIdentifier pokerPlayerIdentifier) throws PokerServiceException, ValidationException;

	Collection<PokerGame> getGames(boolean running) throws PokerServiceException;

	void stopGame(PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException;

	Collection<PokerPlayerIdentifier> getActivePlayers(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier getBigBlindPlayer(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier getSmallBlindPlayer(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	PokerPlayerIdentifier getButtonPlayer(PokerGameIdentifier gameIdentifier) throws PokerServiceException;

	void expectPokerAdminPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException;

	void expectPokerPlayerOrAdminPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException;

	void expectPokerPlayerPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException;

	boolean hasPokerAdminPermission(SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException;

	boolean hasPokerPlayerOrAdminPermission(SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException;

	boolean hasPokerPlayerPermission(SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException;

	void updateGame(PokerGameDto pokerGameDto) throws PokerServiceException, ValidationException;

}
