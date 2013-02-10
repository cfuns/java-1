package de.benjaminborbe.poker.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
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
	public Collection<PokerGameIdentifier> getGames() {
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
	public Collection<PokerCardIdentifier> getCards(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public Collection<PokerCardIdentifier> getCards(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerGameIdentifier createGame(final String name) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerPlayerIdentifier createPlayer(final String name) throws PokerServiceException {
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

}
