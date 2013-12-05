package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerAutoFoldAllGames {

	private final PokerGameDao pokerGameDao;

	private final Logger logger;

	private final PokerAutoFoldGame pokerAutoFoldGame;

	@Inject
	public PokerAutoFoldAllGames(final Logger logger, final PokerGameDao pokerGameDao, final PokerAutoFoldGame pokerAutoFoldGame) {
		this.logger = logger;
		this.pokerGameDao = pokerGameDao;
		this.pokerAutoFoldGame = pokerAutoFoldGame;
	}

	public void processAllGames() {
		try {
			logger.debug("poker auto caller cron iterate of games started");
			final EntityIterator<PokerGameBean> i = pokerGameDao.getEntityIterator();
			while (i.hasNext()) {
				final PokerGameBean game = i.next();
				pokerAutoFoldGame.processGame(game);
			}
			logger.debug("poker auto caller cron iterate of games finished");
		} catch (final EntityIteratorException e) {
			logger.debug(e.getClass().getName(), e);
		} catch (StorageException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}
}
