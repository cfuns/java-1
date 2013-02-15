package de.benjaminborbe.poker.api;

public enum PokerCardColor {

	// Karo
	DIAMONDS(1),

	// Herz
	HEARTS(2),

	// Pik
	SPADES(3),

	// Kreuz
	CLUBS(4);

	private final int value;

	private PokerCardColor(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
