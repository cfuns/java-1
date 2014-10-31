package de.benjaminborbe.poker.table.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable, IsSerializable {

	private static final long serialVersionUID = 8116900440759748074L;

	private String gameId;

	private boolean gameRunning;

	private String gameBid;

	private String gameMaxBid;

	private String gameName;

	private ArrayList<Player> players = new ArrayList<Player>();

	private String gameRound;

	private String gameSmallBlind;

	private String gamePot;

	private String gameBigBlind;

	private ArrayList<Card> boardCards = new ArrayList<Card>();

	private Player activePlayer;

	public Game() {
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(final String gameId) {
		this.gameId = gameId;
	}

	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(final boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public String getGameBid() {
		return gameBid;
	}

	public void setGameBid(final String gameBid) {
		this.gameBid = gameBid;
	}

	public String getGameMaxBid() {
		return gameMaxBid;
	}

	public void setGameMaxBid(final String gameMaxBid) {
		this.gameMaxBid = gameMaxBid;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(final String gameName) {
		this.gameName = gameName;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(final ArrayList<Player> players) {
		this.players = players;
	}

	public String getGameRound() {
		return gameRound;
	}

	public void setGameRound(final String gameRound) {
		this.gameRound = gameRound;
	}

	public String getGameSmallBlind() {
		return gameSmallBlind;
	}

	public void setGameSmallBlind(final String gameSmallBlind) {
		this.gameSmallBlind = gameSmallBlind;
	}

	public String getGamePot() {
		return gamePot;
	}

	public void setGamePot(final String gamePot) {
		this.gamePot = gamePot;
	}

	public String getGameBigBlind() {
		return gameBigBlind;
	}

	public void setGameBigBlind(final String gameBigBlind) {
		this.gameBigBlind = gameBigBlind;
	}

	public ArrayList<Card> getBoardCards() {
		return boardCards;
	}

	public void setBoardCards(final ArrayList<Card> boardCards) {
		this.boardCards = boardCards;
	}

	public Player getActivePlayer() {
		return activePlayer;
	}

	public void setActivePlayer(final Player activePlayer) {
		this.activePlayer = activePlayer;
	}

	public void addPlayer(final Player player) {
		players.add(player);
	}

	public void addCard(final Card Card) {
		boardCards.add(Card);
	}
}
