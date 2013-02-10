package de.benjaminborbe.poker.api;

import de.benjaminborbe.api.IdentifierBase;

public class PokerCardIdentifier extends IdentifierBase<String> {

	public static final String SEPERATOR = "_";

	private final PokerCardColor color;

	private final PokerCardValue value;

	public PokerCardIdentifier(final PokerCardColor color, final PokerCardValue value) {
		super(color.name() + SEPERATOR + value.name());
		this.color = color;
		this.value = value;
	}

	public PokerCardColor getColor() {
		return color;
	}

	public PokerCardValue getValue() {
		return value;
	}

}
