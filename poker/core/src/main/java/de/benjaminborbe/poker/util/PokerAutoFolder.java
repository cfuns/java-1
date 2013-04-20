package de.benjaminborbe.poker.util;

import org.slf4j.Logger;

import javax.inject.Inject;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CurrentTime;

public class PokerAutoFolder {

	private final PokerGameDao pokerGameDao;

	private final Logger logger;

	private final CurrentTime currentTime;

	private final PokerService pokerService;

	@Inject
	public PokerAutoFolder(final Logger logger, final PokerGameDao pokerGameDao, final CurrentTime currentTime, final PokerService pokerService) {
		this.logger = logger;
		this.pokerGameDao = pokerGameDao;
		this.currentTime = currentTime;
		this.pokerService = pokerService;
	}

	public void run() {
		try {
			final EntityIterator<PokerGameBean> i = pokerGameDao.getEntityIterator();
			while (i.hasNext()) {
				final PokerGameBean game = i.next();
				handle(game);
			}
		}
		catch (final EntityIteratorException | StorageException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private void handle(final PokerGameBean game) {
		if (Boolean.TRUE.equals(game.getRunning()) && game.getAutoFoldTimeout() != null && game.getAutoFoldTimeout() > 0) {
			final long timeout = game.getAutoFoldTimeout();
			if ((currentTime.currentTimeMillis() - game.getActivePositionTime().getTimeInMillis()) > timeout) {
				try {
					pokerService.fold(game.getId(), pokerService.getActivePlayer(game.getId()));
				}
				catch (PokerServiceException | ValidationException e) {
					logger.debug(e.getClass().getName(), e);
				}
			}
		}
	}
}
