package de.benjaminborbe.poker.util;

import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.event.PokerPlayerAmountChangedEvent;
import de.benjaminborbe.poker.event.PokerPlayerCreatedEvent;
import de.benjaminborbe.poker.event.PokerPlayerDeletedEvent;
import de.benjaminborbe.poker.event.PokerPlayerScoreChangedEvent;
import de.benjaminborbe.poker.player.PokerPlayerBean;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerEventPoster {

	private final Logger logger;

	private final EventbusService eventbusService;

	@Inject
	public PokerEventPoster(final Logger logger, final EventbusService eventbusService) {
		this.logger = logger;
		this.eventbusService = eventbusService;
	}

	public void firePokerPlayerAmountChangedEvent(final PokerPlayerBean player) {
		postEvent(new PokerPlayerAmountChangedEvent(player.getId(), player.getAmount()));
	}

	public void firePokerPlayerCreatedEvent(final PokerPlayerBean bean) {
		postEvent(new PokerPlayerCreatedEvent(bean));
	}

	public void firePokerPlayerDeletedEvent(final PokerPlayerIdentifier playerIdentifier) {
		postEvent(new PokerPlayerDeletedEvent(playerIdentifier));
	}

	public void firePokerPlayerScoreChangedEvent(final PokerPlayerBean player, final long score) {
		postEvent(new PokerPlayerScoreChangedEvent(player.getId(), score));
	}

	private void postEvent(final Event<?> event) {
		logger.debug("fire event {}", event.getAssociatedType());
		eventbusService.fireEvent(event);
	}

}
