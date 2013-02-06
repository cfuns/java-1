package de.benjaminborbe.poker.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.Game;
import de.benjaminborbe.poker.api.GameIdentifier;
import de.benjaminborbe.poker.api.Player;
import de.benjaminborbe.poker.api.PlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;

@Singleton
public class PokerServiceImpl implements PokerService {

	private final Logger logger;

	@Inject
	public PokerServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Collection<GameIdentifier> getGames() {
		logger.debug("isGameRunning");
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

}
