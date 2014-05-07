package de.benjaminborbe.poker.table.client.model;

import java.io.Serializable;

public class Card implements Serializable {

	String cardName;

	public String getCard() {
		return cardName;
	}

	public void setCard(final String card) {
		this.cardName = card;
	}
}
