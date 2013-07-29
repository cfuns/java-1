package de.benjaminborbe.poker.util;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.date.CurrentTime;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerAutoCaller {

	private final PokerGameDao pokerGameDao;

	private final Logger logger;

	private final CurrentTime currentTime;

	private final PokerService pokerService;

	@Inject
	public PokerAutoCaller(final Logger logger, final PokerGameDao pokerGameDao, final CurrentTime currentTime, final PokerService pokerService) {
		this.logger = logger;
		this.pokerGameDao = pokerGameDao;
		this.currentTime = currentTime;
		this.pokerService = pokerService;
	}

	public void run() {
		try {
			logger.debug("poker auto caller cron iterate of games started");
			final EntityIterator<PokerGameBean> i = pokerGameDao.getEntityIterator();
			while (i.hasNext()) {
				final PokerGameBean game = i.next();
				handle(game);
			}
			logger.debug("poker auto caller cron iterate of games finished");
		} catch (final EntityIteratorException e) {
			logger.debug(e.getClass().getName(), e);
		} catch (StorageException e) {
			logger.debug(e.getClass().getName(), e);
		}
	}

	private void handle(final PokerGameBean game) {
		logger.debug("poker auto caller handle game " + game.getId() + " started");
		if (Boolean.TRUE.equals(game.getRunning()) && game.getActivePositionTime() != null && game.getAutoCallTimeout() != null && game.getAutoCallTimeout() > 0) {
			logger.debug("auto caller game " + game.getId());
			final long timeout = game.getAutoCallTimeout();
			if ((currentTime.currentTimeMillis() - game.getActivePositionTime().getTimeInMillis()) > timeout) {
				logger.debug("timeout reached => call");
				try {
					pokerService.call(game.getId(), pokerService.getActivePlayer(game.getId()));
				} catch (PokerServiceException e) {
					logger.debug(e.getClass().getName(), e);
				} catch (ValidationException e) {
					logger.debug(e.getClass().getName(), e);
				}
			} else {
				logger.debug("timeout not reached => skip");
			}
		}
		logger.debug("poker auto caller handle game " + game.getId() + " finished");
	}
}
