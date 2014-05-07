package de.benjaminborbe.poker.table.client.model;

import java.io.Serializable;

public class Card implements Serializable {

	private String cardName;

	public Card() {
	}

	public String getCard() {
		return cardName;
	}

	public void setCard(final String card) {
		this.cardName = card;
	}
}
