package de.benjaminborbe.poker.api;

import java.util.Calendar;
import java.util.List;

public interface PokerGame {

	PokerGameIdentifier getId();

	Boolean getRunning();

	Long getBigBlind();

	Long getSmallBlind();

	List<PokerPlayerIdentifier> getPlayers();

	List<PokerCardIdentifier> getBoardCards();

	Long getPot();

	String getName();

	Long getRound();

	Long getBet();

	Long getMaxBid();

	List<PokerCardIdentifier> getCards();

	Calendar getActivePositionTime();

	Long getAutoFoldTimeout();

	Boolean getCreditsNegativeAllowed();

	Double getMinRaiseFactor();

	Double getMaxRaiseFactor();

	Long getScore();
}
