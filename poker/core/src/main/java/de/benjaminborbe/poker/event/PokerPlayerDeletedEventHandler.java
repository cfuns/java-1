package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerServiceException;

public interface PokerPlayerDeletedEventHandler extends EventHandler {

	void onPlayerDeleted(PokerPlayerIdentifier playerIdentifier) throws AnalyticsServiceException, LoginRequiredException, PermissionDeniedException, PokerServiceException;
}
