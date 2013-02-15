package de.benjaminborbe.poker.gui.util;

import java.util.ResourceBundle;

import de.benjaminborbe.poker.api.PokerCardIdentifier;

public class PokerCardTranslater {

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

	public String translate(final PokerCardIdentifier pokerCardIdentifier) {
		final StringBuilder sb = new StringBuilder();
		sb.append(resourceBundle.getString(pokerCardIdentifier.getColor().name()));
		sb.append(" ");
		sb.append(resourceBundle.getString(pokerCardIdentifier.getValue().name()));
		return sb.toString();
	}
}
