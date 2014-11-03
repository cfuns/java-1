package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.player.PokerPlayerBean;

public interface PokerPlayerCreatedEventHandler extends EventHandler {

	void onPlayerCreated(PokerPlayerBean pokerPlayerBean) throws PokerServiceException, PermissionDeniedException, AnalyticsServiceException, LoginRequiredException, ValidationException;
}
