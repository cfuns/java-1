package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardIdentifier;

import javax.inject.Inject;
import java.util.Comparator;

public class PokerCardComparator implements Comparator<PokerCardIdentifier> {

	private final PokerValueComparator value;

	private final PokerColorComparator color;

	@Inject
	public PokerCardComparator(final PokerValueComparator value, final PokerColorComparator color) {
		this.value = value;
		this.color = color;
	}

	@Override
	public int compare(final PokerCardIdentifier arg0, final PokerCardIdentifier arg1) {
		if (value.compare(arg0.getValue(), arg1.getValue()) == 0) {
			return color.compare(arg0.getColor(), arg1.getColor());
		} else {
			return value.compare(arg0.getValue(), arg1.getValue());
		}
	}
}
