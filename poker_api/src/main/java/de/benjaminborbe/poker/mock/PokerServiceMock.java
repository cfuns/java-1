package de.benjaminborbe.poker.mock;

import java.util.Collection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.Game;
import de.benjaminborbe.poker.api.GameIdentifier;
import de.benjaminborbe.poker.api.Player;
import de.benjaminborbe.poker.api.PlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;

@Singleton
public class PokerServiceMock implements PokerService {

	@Inject
	public PokerServiceMock() {
	}

	@Override
	public Collection<GameIdentifier> getGames() {
		return null;
	}

	@Override
	public Game getGame(final GameIdentifier gameIdentifier) {
		return null;
	}

	@Override
	public Collection<PlayerIdentifier> getPlayers(final GameIdentifier gameIdentifier) {
		return null;
	}

	@Override
	public Player getPlayer(final PlayerIdentifier playerIdentifier) {
		return null;
	}

	@Override
	public GameIdentifier createGame() {
		return null;
	}

	@Override
	public void startGame(final GameIdentifier gameIdentifier) {
	}

	@Override
	public GameIdentifier createGameIdentifier(String gameId) throws PokerServiceException {
		return null;
	}

}
