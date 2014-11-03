package de.benjaminborbe.poker.player;

import de.benjaminborbe.poker.event.PokerPlayerCreatedEvent;
import de.benjaminborbe.poker.event.PokerPlayerCreatedEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PokerPlayerCreatedAnalyticsEventHandler implements PokerPlayerCreatedEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(PokerPlayerCreatedAnalyticsEventHandler.class);

	@Inject
	public PokerPlayerCreatedAnalyticsEventHandler() {
	}

	@Override
	public void onPlayerCreated(final PokerPlayerCreatedEvent pokerPlayerCreatedEvent) {
		logger.debug("onPlayerCreated: {}", pokerPlayerCreatedEvent.getPokerPlayerIdentifier());
	}
}
