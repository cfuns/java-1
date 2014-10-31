package de.benjaminborbe.poker.reset;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.gamecreator.PokerGameCreator;
import de.benjaminborbe.poker.player.PokerPlayerBean;
import de.benjaminborbe.poker.player.PokerPlayerDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PokerEventReseter {

	private static final Logger logger = LoggerFactory.getLogger(PokerEventReseter.class);

	private static final String DEFAULT_POKER_GAME_NAME = "Game 1";

	private final PokerGameDao pokerGameDao;

	private final PokerPlayerDao pokerPlayerDao;

	private final PokerGameCreator pokerGameCreator;

	@Inject
	public PokerEventReseter(
		final PokerGameDao pokerGameDao,
		final PokerPlayerDao pokerPlayerDao,
		final PokerGameCreator pokerGameCreator
	) {
		this.pokerGameDao = pokerGameDao;
		this.pokerPlayerDao = pokerPlayerDao;
		this.pokerGameCreator = pokerGameCreator;
	}

	public void reset() throws StorageException, EntityIteratorException, ValidationException {
		logger.debug("reset poker event - started");
		deleteAllGames();
		deleteAllPlayer();
		createNewGame();
		logger.debug("reset poker event - finished");
	}

	private void createNewGame() throws ValidationException, StorageException {
		logger.debug("createNewGame");
		final PokerGameDto pokerGameDto = new PokerGameDto();
		pokerGameDto.setName(DEFAULT_POKER_GAME_NAME);
		pokerGameCreator.createGame(pokerGameDto);
	}

	private void deleteAllPlayer() throws EntityIteratorException, StorageException {
		logger.debug("deleteAllPlayer");
		final EntityIterator<PokerPlayerBean> entityIterator = pokerPlayerDao.getEntityIterator();
		while (entityIterator.hasNext()) {
			pokerPlayerDao.delete(entityIterator.next());
		}
	}

	private void deleteAllGames() throws StorageException, EntityIteratorException {
		logger.debug("deleteAllGames");
		final EntityIterator<PokerGameBean> entityIterator = pokerGameDao.getEntityIterator();
		while (entityIterator.hasNext()) {
			pokerGameDao.delete(entityIterator.next());
		}
	}

}
