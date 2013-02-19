package de.benjaminborbe.poker.api;

public interface PokerPlayer {

	PokerPlayerIdentifier getId();

	String getName();

	Long getAmount();

	PokerGameIdentifier getGame();

	Long getBet();

	String getToken();

}
