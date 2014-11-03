package de.benjaminborbe.poker.table.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class Card implements Serializable, IsSerializable {

	private static final long serialVersionUID = -280830516312553526L;

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
