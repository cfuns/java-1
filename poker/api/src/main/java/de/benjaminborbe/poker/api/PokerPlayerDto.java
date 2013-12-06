package de.benjaminborbe.poker.api;

import de.benjaminborbe.authentication.api.UserIdentifier;

import java.util.Collection;
import java.util.List;

public class PokerPlayerDto implements PokerPlayer {

	private PokerPlayerIdentifier id;

	private PokerGameIdentifier game;

	private Collection<UserIdentifier> owners;

	private String name;

	private Long amount;

	private Long bet;

	private String token;

	private Long score;

	private List<PokerCardIdentifier> cards;

	public Long getScore() {
		return score;
	}

	public void setScore(final Long score) {
		this.score = score;
	}

	@Override
	public PokerPlayerIdentifier getId() {
		return id;
	}

	public void setId(final PokerPlayerIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Long getAmount() {
		return amount;
	}

	public void setAmount(final Long amount) {
		this.amount = amount;
	}

	@Override
	public PokerGameIdentifier getGame() {
		return game;
	}

	public void setGame(final PokerGameIdentifier game) {
		this.game = game;
	}

	@Override
	public Long getBet() {
		return bet;
	}

	public void setBet(final Long bet) {
		this.bet = bet;
	}

	@Override
	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public Collection<UserIdentifier> getOwners() {
		return owners;
	}

	public void setOwners(final Collection<UserIdentifier> owners) {
		this.owners = owners;
	}

	@Override
	public List<PokerCardIdentifier> getCards() {
		return cards;
	}

	public void setCards(final List<PokerCardIdentifier> cards) {
		this.cards = cards;
	}

}
