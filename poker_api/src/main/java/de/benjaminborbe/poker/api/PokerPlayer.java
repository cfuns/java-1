package de.benjaminborbe.poker.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface PokerPlayer {

	PokerPlayerIdentifier getId();

	String getName();

	Long getAmount();

	PokerGameIdentifier getGame();

	Long getBet();

	String getToken();

	Collection<UserIdentifier> getOwners();

}
