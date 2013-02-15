package de.benjaminborbe.poker.gui.util;

import java.util.ResourceBundle;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;

public class PokerCardTranslater {

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

	public Widget translate(final PokerCardIdentifier pokerCardIdentifier) {
		final ListWidget widgets = new ListWidget();
		widgets.add(new SpanWidget(resourceBundle.getString(pokerCardIdentifier.getColor().name())).addClass(pokerCardIdentifier.getColor().name().toLowerCase()));
		widgets.add(" ");
		widgets.add(new SpanWidget(resourceBundle.getString(pokerCardIdentifier.getValue().name())));
		return widgets;
	}
}
