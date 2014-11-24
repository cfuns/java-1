package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.eventbus.api.Event;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.player.PokerPlayerBean;

public class PokerPlayerCreatedEvent extends Event<PokerPlayerCreatedEventHandler> {

	public static final Type<PokerPlayerCreatedEventHandler> TYPE = new Type<PokerPlayerCreatedEventHandler>(PokerPlayerCreatedEventHandler.class);

	private final PokerPlayerBean player;

	public PokerPlayerCreatedEvent(final PokerPlayerBean player) {
		this.player = player;
	}

	@Override
	public Type<PokerPlayerCreatedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	public void dispatch(final PokerPlayerCreatedEventHandler handler) throws LoginRequiredException, PermissionDeniedException, PokerServiceException, AnalyticsServiceException, ValidationException {
		handler.onPlayerCreated(player);
	}
}
