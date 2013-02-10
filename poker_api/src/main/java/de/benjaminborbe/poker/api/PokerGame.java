package de.benjaminborbe.poker.api;

import java.util.List;

public interface PokerGame {

	PokerGameIdentifier getId();

	Boolean getRunning();

	PokerPlayerIdentifier getActivePlayer();

	Long getBigBlind();

	Long getSmallBlind();

	List<PokerPlayerIdentifier> getPlayers();

	List<PokerCardIdentifier> getCards();
}
