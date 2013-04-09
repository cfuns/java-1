package de.benjaminborbe.poker.api;

import java.util.List;

public class PokerGameDto implements PokerGame {

	private Long maxBid;

	private Long bet;

	private Long round;

	private String name;

	private Long pot;

	private List<PokerCardIdentifier> cards;

	private List<PokerPlayerIdentifier> players;

	private Long smallBlind;

	private Long bigBlind;

	private Boolean running;

	private PokerGameIdentifier id;

	@Override
	public Long getMaxBid() {
		return maxBid;
	}

	public void setMaxBid(final Long maxBid) {
		this.maxBid = maxBid;
	}

	@Override
	public Long getBet() {
		return bet;
	}

	public void setBet(final Long bet) {
		this.bet = bet;
	}

	@Override
	public Long getRound() {
		return round;
	}

	public void setRound(final Long round) {
		this.round = round;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Long getPot() {
		return pot;
	}

	public void setPot(final Long pot) {
		this.pot = pot;
	}

	@Override
	public List<PokerCardIdentifier> getCards() {
		return cards;
	}

	public void setCards(final List<PokerCardIdentifier> cards) {
		this.cards = cards;
	}

	@Override
	public List<PokerPlayerIdentifier> getPlayers() {
		return players;
	}

	public void setPlayers(final List<PokerPlayerIdentifier> players) {
		this.players = players;
	}

	@Override
	public Long getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(final Long smallBlind) {
		this.smallBlind = smallBlind;
	}

	@Override
	public Long getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(final Long bigBlind) {
		this.bigBlind = bigBlind;
	}

	@Override
	public Boolean getRunning() {
		return running;
	}

	public void setRunning(final Boolean running) {
		this.running = running;
	}

	@Override
	public PokerGameIdentifier getId() {
		return id;
	}

	public void setId(final PokerGameIdentifier id) {
		this.id = id;
	}
}
