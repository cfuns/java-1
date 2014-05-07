package de.benjaminborbe.poker.table.client.vectorobject;

import de.benjaminborbe.poker.table.client.vectorgroup.CardStack;
import de.benjaminborbe.poker.table.client.vectorgroup.CoinStack;
import org.vaadin.gwtgraphics.client.Group;

public class PlayerObject extends Group {

	private CardStack playerCards;

	private CoinStack coinStack;

	public int getCredits() {
		return credits;
	}

	public void setCredits(final int credits) {
		this.credits = credits;
	}

	private int credits;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(final String playerName) {
		this.playerName = playerName;
	}

	private String playerName;

	public CoinStack getCoinStack() {
		return coinStack;
	}

	public void setCoinStack(final CoinStack coinStack) {
		this.coinStack = coinStack;
	}

	private int playerPositionX = 0, playerPositionY = 0;

	private int angle = 0;

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	boolean active = false;

	public PlayerObject(int phi, int rotAngle, int width, int height, int canvasWidth, int canvasHeigth, boolean active, int credits) {
		super();
		this.angle = rotAngle;
		this.playerPositionX = (int) Math.round((Math.cos(phi) * width) + canvasWidth / 2);
		this.playerPositionY = (int) Math.round((Math.sin(phi) * height) + canvasHeigth / 2);
		this.playerCards = new CardStack(2);
		this.coinStack = new CoinStack(credits, playerPositionX, playerPositionY);
		this.active = active;
		this.add(playerCards);
		this.add(this.coinStack);

	}

	public CardStack getPlayerCards() {
		return playerCards;
	}

	public void setPlayerCards(CardStack playerCards) {
		this.playerCards = playerCards;
	}

	public int getPlayerPosX() {
		return this.playerPositionX;
	}

	public int getPlayerPosY() {
		return this.playerPositionY;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

}
