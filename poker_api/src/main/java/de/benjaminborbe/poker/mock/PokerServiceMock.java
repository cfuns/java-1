package de.benjaminborbe.poker.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;

@Singleton
public class PokerServiceMock implements PokerService {

	@Inject
	public PokerServiceMock() {
	}

	@Override
	public Collection<PokerGameIdentifier> getGameIdentifiers() {
		return null;
	}

	@Override
	public PokerGame getGame(final PokerGameIdentifier gameIdentifier) {
		return null;
	}

	@Override
	public Collection<PokerPlayerIdentifier> getPlayers(final PokerGameIdentifier gameIdentifier) {
		return null;
	}

	@Override
	public PokerPlayer getPlayer(final PokerPlayerIdentifier playerIdentifier) {
		return null;
	}

	@Override
	public void startGame(final PokerGameIdentifier gameIdentifier) {
	}

	@Override
	public PokerGameIdentifier createGameIdentifier(final String gameId) throws PokerServiceException {
		return null;
	}

	@Override
	public Collection<PokerCardIdentifier> getHandCards(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public Collection<PokerCardIdentifier> getBoardCards(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerGameIdentifier createGame(final String name, final long blind) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerPlayerIdentifier createPlayerIdentifier(final String id) throws PokerServiceException {
		return null;
	}

	@Override
	public void joinGame(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
	}

	@Override
	public void leaveGame(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
	}

	@Override
	public PokerPlayerIdentifier getActivePlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public void fold(final PokerGameIdentifier pokerGameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
	}

	@Override
	public void call(final PokerGameIdentifier pokerGameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
	}

	@Override
	public void raise(final PokerGameIdentifier pokerGameIdentifier, final PokerPlayerIdentifier playerIdentifier, final long amount) throws PokerServiceException {
	}

	@Override
	public Collection<PokerPlayer> getPlayers() throws PokerServiceException {
		return null;
	}

	@Override
	public void deleteGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
	}

	@Override
	public void deletePlayer(final PokerPlayerIdentifier pokerPlayerIdentifier) throws PokerServiceException {
	}

	@Override
	public Collection<PokerGame> getGames(final boolean running) throws PokerServiceException {
		return null;
	}

	@Override
	public void stopGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException {
	}

	@Override
	public PokerPlayerIdentifier createPlayer(final String name, final long credits) throws PokerServiceException, ValidationException {
		return null;
	}

	@Override
	public Collection<PokerPlayerIdentifier> getActivePlayers(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerPlayerIdentifier getBigBlindPlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerPlayerIdentifier getSmallBlindPlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerPlayerIdentifier getButtonPlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public void expectPokerAdminRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException {
	}

	@Override
	public void expectPokerPlayerOrAdminRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException {
	}

	@Override
	public void expectPokerPlayerRole(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException {
	}

	@Override
	public boolean hasPokerAdminRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException {
		return false;
	}

	@Override
	public boolean hasPokerPlayerOrAdminRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException {
		return false;
	}

	@Override
	public boolean hasPokerPlayerRole(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException {
		return false;
	}

	@Override
	public Collection<PokerGame> getGames() throws PokerServiceException {
		return null;
	}

}
