package de.benjaminborbe.poker.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

import java.util.Collection;
import java.util.List;

public interface PokerPlayer {

	PokerPlayerIdentifier getId();

	String getName();

	Long getAmount();

	PokerGameIdentifier getGame();

	Long getBet();

	String getToken();

	Collection<UserIdentifier> getOwners();

	List<PokerCardIdentifier> getCards();

	Long getScore();

}
