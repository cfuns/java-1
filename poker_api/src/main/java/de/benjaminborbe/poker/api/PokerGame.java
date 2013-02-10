package de.benjaminborbe.poker.api;

public interface PokerGame {

	PokerGameIdentifier getId();

	Boolean getRunning();

	PokerPlayerIdentifier getActivePlayer();

	Long getBigBlind();

	Long getSmallBlind();
}
