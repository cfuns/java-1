package de.benjaminborbe.poker.game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class PokerGameBean extends EntityBase<PokerGameIdentifier> implements PokerGame, HasCreated, HasModified {

	private static final long serialVersionUID = -1631188424667532085L;

	private PokerGameIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	private Boolean running;

	private Long bigBlind;

	private Long smallBlind;

	private Long round;

	private Integer activePosition;

	private Integer buttonPosition;

	private List<PokerPlayerIdentifier> players = new ArrayList<PokerPlayerIdentifier>();

	private List<PokerPlayerIdentifier> activePlayers = new ArrayList<PokerPlayerIdentifier>();

	private List<PokerCardIdentifier> cards = new ArrayList<PokerCardIdentifier>();

	private List<PokerCardIdentifier> boardCards = new ArrayList<PokerCardIdentifier>();

	private Integer cardPosition;

	private Long pot;

	private Long bet;

	@Override
	public Long getBigBlind() {
		return bigBlind;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public PokerGameIdentifier getId() {
		return id;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Boolean getRunning() {
		return running;
	}

	@Override
	public Long getSmallBlind() {
		return smallBlind;
	}

	public void setBigBlind(final Long bigBlind) {
		this.bigBlind = bigBlind;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public void setId(final PokerGameIdentifier id) {
		this.id = id;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setRunning(final Boolean running) {
		this.running = running;
	}

	public void setSmallBlind(final Long smallBlind) {
		this.smallBlind = smallBlind;
	}

	@Override
	public List<PokerPlayerIdentifier> getPlayers() {
		return players;
	}

	public void setPlayers(final List<PokerPlayerIdentifier> players) {
		this.players = players;
	}

	@Override
	public List<PokerCardIdentifier> getCards() {
		return cards;
	}

	public void setCards(final List<PokerCardIdentifier> cards) {
		this.cards = cards;
	}

	@Override
	public Long getPot() {
		return pot;
	}

	public void setPot(final Long pot) {
		this.pot = pot;
	}

	public Integer getCardPosition() {
		return cardPosition;
	}

	public void setCardPosition(final Integer cardPosition) {
		this.cardPosition = cardPosition;
	}

	public Integer getButtonPosition() {
		return buttonPosition;
	}

	public void setButtonPosition(final Integer buttonPosition) {
		this.buttonPosition = buttonPosition;
	}

	public Integer getActivePosition() {
		return activePosition;
	}

	public void setActivePosition(final Integer activePosition) {
		this.activePosition = activePosition;
	}

	public Long getRound() {
		return round;
	}

	public void setRound(final Long round) {
		this.round = round;
	}

	public Long getBet() {
		return bet;
	}

	public void setBet(final Long bet) {
		this.bet = bet;
	}

	public List<PokerPlayerIdentifier> getActivePlayers() {
		return activePlayers;
	}

	public void setActivePlayers(final List<PokerPlayerIdentifier> activePlayers) {
		this.activePlayers = activePlayers;
	}

	public List<PokerCardIdentifier> getBoardCards() {
		return boardCards;
	}

	public void setBoardCards(final List<PokerCardIdentifier> boardCards) {
		this.boardCards = boardCards;
	}

}
