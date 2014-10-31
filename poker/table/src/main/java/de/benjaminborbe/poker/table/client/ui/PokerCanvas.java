package de.benjaminborbe.poker.table.client.ui;

import com.google.gwt.user.client.Timer;
import de.benjaminborbe.poker.table.client.model.Card;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.model.Player;
import de.benjaminborbe.poker.table.client.vectorgroup.CardStack;
import de.benjaminborbe.poker.table.client.vectorgroup.CoinStack;
import de.benjaminborbe.poker.table.client.vectorobject.CardObject;
import de.benjaminborbe.poker.table.client.vectorobject.CoinObject;
import de.benjaminborbe.poker.table.client.vectorobject.PlayerObject;
import de.benjaminborbe.poker.table.client.vectorobject.PokerTable;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.Image;
import org.vaadin.gwtgraphics.client.animation.Animate;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;

public final class PokerCanvas extends DrawingArea {

	private Rectangle canvasFrame;

	private Group playerPositions = new Group();

	private Group river = new Group();

	private Group pot = new Group();

	private Group activePlayerGroup = new Group();

	private CardStack cardStack = new CardStack(32);

	private int canvasWidth = 0;

	private int canvasHeight = 0;

	private int potPositionX = 0;

	private int potPositionY = 0;

	private int frequenz = 1000;

	int counter = 0;

	private Group playerNames = new Group();

	private Group gameText = new Group();

	boolean runFlag = false;

	private int thisRound = 0;

	private Group winner = new Group();

	// ------------Public---------------------
	public PokerCanvas(int canvasWidth, int canvasHeight) {
		super(canvasWidth, canvasHeight);
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.canvasFrame = new Rectangle(0, 0, canvasWidth, canvasHeight);
		this.add(canvasFrame);

		this.potPositionX = (canvasWidth / 2) - 100;
		this.potPositionY = (canvasHeight / 2) + 100;

		this.add(new PokerTable(canvasWidth, canvasHeight));
		// this.createCardStack();
		this.add(river);
		this.add(playerPositions);
		this.add(pot);

		this.add(activePlayerGroup);
		this.add(playerNames);
		this.add(gameText);

		// deliverFlop();

	}

	public void setRunFlag(final boolean runFlag) {
		this.runFlag = runFlag;
	}

	public boolean isRunFlag() {
		return runFlag;
	}

	// ------------Private---------------------

	public void createCardStack() {
		for (int i = 0; i < cardStack.getStackSize(); i++) {
			this.cardStack.add(new CardObject(i, canvasWidth / 2, (canvasHeight / 2) + 10));
		}
		this.add(cardStack);
	}

	public void updateBoardCards(ArrayList<Card> cards) {
		int i = 0;
		river.clear();
		for (Card card : cards) {
			CardObject co = new CardObject(i, (canvasWidth / 2 - 150) + (50 * i + 10 * i), canvasHeight / 2 - 75);
			if (null != co.getImage() || "" != co.getImage()) {
				co.setImage(cards.get(i).getCard());
			}
			co.switchImage();
			river.add(co);
			i++;
		}
	}

	public void updatePlayerPositions(ArrayList<Player> list) {
		playerPositions.clear();
		int phi = 90;
		int i = 0;
		for (Player player : list) {
			PlayerObject po = new PlayerObject(i, phi, 500, 250, canvasWidth, canvasHeight, player.isActivePlayer(), player.getCredits());
			po.setPlayerName(player.getUsername());
			po.setCredits(player.getCredits());
			int j = 0;
			for (Card card : player.getCards()) {
				CardObject co = new CardObject(j, po.getPlayerPosX() + (j * 40), po.getPlayerPosY() + (j * 20));
				co.setImage(card.getCard());
				co.switchImage();
				if (!runFlag) {
					new Animate(co, "rotation", 0, po.getAngle(), 10).start();
				}
				po.getPlayerCards().add(co);
				j++;
			}
			playerPositions.add(po);
			phi = phi + 60;
			i++;
		}
	}

	public void updatePot(Game game) {
		pot.clear();
		if (runFlag) {

			int potValue = Integer.parseInt(game.getGamePot());
			int diffx = 0;
			int diffy = 0;
			int positionChange = 0;
			int result = (int) potValue/100;
			for (int i = 0; i <= result; i++) {
				pot.add(new CoinObject(1, potPositionX + diffx, potPositionY + positionChange + diffy));
				diffy = diffy - 2;
				if(diffy % 20 == 0){
					diffx = diffx + 10;
					diffy = 0;
					if(diffx == 50){
						positionChange = positionChange + 5;
						diffx = 0;
					}
				}
			}
			Text gameName = new Text(potPositionX, potPositionY - 40, String.valueOf(potValue));
			gameName.setFillColor("black");
			pot.add(gameName);
		}

	}

	public void updateRound(Game game) {
		thisRound = Integer.parseInt(game.getGameRound());
	}

	public void updatePlayerPot() {
		if (runFlag) {
			for (int i = 0; i < playerPositions.getVectorObjectCount(); i++) {
				PlayerObject po = (PlayerObject) playerPositions.getVectorObject(i);
				po.getCoinStack().clear();
				po.setCoinStack(new CoinStack(po.getCredits(), po.getPlayerPosX(), po.getPlayerPosY()));
			}
		}
	}

	public void updateWinner() {
		winner.clear();
		if (runFlag) {
			if (playerPositions.getVectorObjectCount() == 1) {
				PlayerObject po = (PlayerObject) playerPositions.getVectorObject(0);
				Text gameName = new Text(po.getPlayerPosX(), po.getPlayerPosY(), "WINNER");
				gameName.setFillColor("red");
				winner.add(gameName);
			}
		}
	}

	public void updateGameText(Game game) {
		gameText.clear();
		playerNames.clear();
		Text gameName = new Text(10, 20, game.getGameName());
		gameName.setFillColor("black");
		gameText.add(gameName);

		Text gameRound = new Text(10, 45, "Round: " + game.getGameRound());
		gameRound.setFillColor("black");
		gameText.add(gameRound);

		for (int i = 0; i < playerPositions.getVectorObjectCount(); i++) {
			PlayerObject po = (PlayerObject) playerPositions.getVectorObject(i);
			Text playerName = new Text(po.getPlayerPosX() + 10, po.getPlayerPosY() - 20, po.getPlayerName());
			playerName.setFillColor("black");
			playerNames.add(playerName);
			Text creditText = new Text(po.getPlayerPosX() + 10, po.getPlayerPosY() - 40, String.valueOf(po.getCredits()));
			creditText.setFillColor("black");
			playerNames.add(creditText);
		}

	}

	public void updateActivePlayer() {
		activePlayerGroup.clear();
		PlayerObject po;
		for (int i = 0; i < playerPositions.getVectorObjectCount(); i++) {
			po = (PlayerObject) playerPositions.getVectorObject(i);
			if (po.isActive()) {
				CoinObject co = new CoinObject(0, po.getPlayerPosX() - 20, po.getPlayerPosY() - 30);
				co.setRadius(10);
				co.setFillColor("red");
				activePlayerGroup.add(co);
			}
		}
	}

	private void deliverAnimation() {
		counter = 0;
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (runFlag) {
					if (counter < playerPositions.getVectorObjectCount()) {

						PlayerObject po = (PlayerObject) playerPositions.getVectorObject(counter);
						Image object = cardStack.getNextCard(po.getPlayerCards());
						new Animate(object, "rotation", 0, po.getAngle(), frequenz).start();
						new Animate(object, "x", object.getX(), po.getPlayerPosX(), frequenz).start();
						new Animate(object, "y", object.getY(), po.getPlayerPosY(), frequenz).start();
						counter++;
						if (32 - playerPositions.getVectorObjectCount() * 2 == cardStack.getVectorObjectCount()) {
							runFlag = false;
							deliverFlop();
						}
					} else {
						counter = 0;
					}
				}
			}
		};
		timer.scheduleRepeating(frequenz);
	}

	private void deliverFlop() {
		counter = 0;
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (runFlag) {
					if (counter < 3) {
						Image object = cardStack.getNextCard(river);
						new Animate(object, "x", object.getX(), (canvasWidth / 2 - 150) + (50 * counter + 10 * counter), frequenz).start();
						new Animate(object, "y", object.getY(), canvasHeight / 2 - 75, frequenz).start();
						counter++;
					} else {
						this.cancel();
						turnCardsOff();
					}
				}
			}
		};
		timer.scheduleRepeating(frequenz);
	}

	private void turnCardsOff() {
		counter = 0;
		Timer timer = new Timer() {

			@Override
			public void run() {
				for (int i = 0; i < river.getVectorObjectCount(); i++) {
					Image object = (Image) river.getVectorObject(i);
					new Animate(object, "width", object.getWidth(), 0, frequenz).start();
				}
				this.cancel();
				turnCardsOn();
			}
		};
		timer.scheduleRepeating(frequenz);
	}

	private void turnCardsOn() {
		counter = 0;
		Timer timer = new Timer() {

			@Override
			public void run() {
				for (int i = 0; i < river.getVectorObjectCount(); i++) {
					CardObject object = (CardObject) river.getVectorObject(i);
					object.switchImage();
					new Animate(object, "width", 0, 50, frequenz).start();
				}
				this.cancel();
			}
		};
		timer.scheduleRepeating(frequenz);
	}

}
