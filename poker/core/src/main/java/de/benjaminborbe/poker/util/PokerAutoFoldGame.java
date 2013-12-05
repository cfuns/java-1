package de.benjaminborbe.poker.util;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.game.PokerGameBean;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Calendar;

public class PokerAutoFoldGame {

	private final Logger logger;

	private final PokerTimecheck pokerTimecheck;

	private final PokerService pokerService;

	@Inject
	public PokerAutoFoldGame(final Logger logger, final PokerService pokerService, final PokerTimecheck pokerTimecheck) {
		this.logger = logger;
		this.pokerTimecheck = pokerTimecheck;
		this.pokerService = pokerService;
	}

	public void processGame(final PokerGameBean game) {
		final PokerGameIdentifier pokerGameIdentifier = game.getId();
		logger.debug("poker auto caller processGame game " + pokerGameIdentifier + " started");
		if (Boolean.TRUE.equals(game.getRunning())) {
			final Long autoCallTimeout = game.getAutoFoldTimeout();
			final Calendar activePositionTime = game.getActivePositionTime();
			if (activePositionTime != null && autoCallTimeout != null && autoCallTimeout > 0) {
				logger.debug("auto caller game " + pokerGameIdentifier);
				if (pokerTimecheck.timeoutReached(activePositionTime, autoCallTimeout)) {
					try {
						logger.debug("timeout reached => fold");
						pokerService.fold(pokerGameIdentifier, pokerService.getActivePlayer(pokerGameIdentifier));
					} catch (PokerServiceException e) {
						logger.debug(e.getClass().getName(), e);
					} catch (ValidationException e) {
						logger.debug(e.getClass().getName(), e);
					}
				} else {
					logger.debug("timeout not reached => skip");
				}
			}
			logger.debug("poker auto caller processGame game " + pokerGameIdentifier + " finished");
		}
	}
}
