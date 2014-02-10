package de.benjaminborbe.poker.game;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

	private Calendar activePositionTime;

	private Integer buttonPosition;

	private List<PokerPlayerIdentifier> players = new ArrayList<PokerPlayerIdentifier>();

	private List<PokerPlayerIdentifier> activePlayers = new ArrayList<PokerPlayerIdentifier>();

	private List<PokerCardIdentifier> cards = new ArrayList<PokerCardIdentifier>();

	private List<PokerCardIdentifier> boardCards = new ArrayList<PokerCardIdentifier>();

	private Integer cardPosition;

	private Long pot;

	private Long bet;

	private Long maxBid;

	private Double maxRaiseFactor;

	private Double minRaiseFactor;

	private Boolean creditsNegativeAllowed;

	private Long autoFoldTimeout;

	private Long score;

	private Long startCredits;

	public Long getStartCredits() {
		return startCredits;
	}

	public void setStartCredits(final Long startCredits) {
		this.startCredits = startCredits;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(final Long score) {
		this.score = score;
	}

	public Boolean getCreditsNegativeAllowed() {
		return creditsNegativeAllowed;
	}

	public void setCreditsNegativeAllowed(final Boolean creditsNegativeAllowed) {
		this.creditsNegativeAllowed = creditsNegativeAllowed;
	}

	public Double getMaxRaiseFactor() {
		return maxRaiseFactor;
	}

	public void setMaxRaiseFactor(final Double maxRaiseFactor) {
		this.maxRaiseFactor = maxRaiseFactor;
	}

	public Double getMinRaiseFactor() {
		return minRaiseFactor;
	}

	public void setMinRaiseFactor(final Double minRaiseFactor) {
		this.minRaiseFactor = minRaiseFactor;
	}

	@Override
	public Long getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(final Long bigBlind) {
		this.bigBlind = bigBlind;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public PokerGameIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final PokerGameIdentifier id) {
		this.id = id;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Boolean getRunning() {
		return running;
	}

	public void setRunning(final Boolean running) {
		this.running = running;
	}

	@Override
	public Long getSmallBlind() {
		return smallBlind;
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

	@Override
	public Long getRound() {
		return round;
	}

	public void setRound(final Long round) {
		this.round = round;
	}

	@Override
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

	@Override
	public Long getMaxBid() {
		return maxBid;
	}

	public void setMaxBid(final Long maxBid) {
		this.maxBid = maxBid;
	}

	@Override
	public Calendar getActivePositionTime() {
		return activePositionTime;
	}

	public void setActivePositionTime(final Calendar activePositionTime) {
		this.activePositionTime = activePositionTime;
	}

	@Override
	public Long getAutoFoldTimeout() {
		return autoFoldTimeout;
	}

	public void setAutoFoldTimeout(final Long autoFoldTimeout) {
		this.autoFoldTimeout = autoFoldTimeout;
	}

}
