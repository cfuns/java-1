package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PokerCardComparatorUnitTest {

	@Test
	public void testColorEquals() throws Exception {
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator comparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		assertEquals(0, comparator.compare(buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FOUR), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FOUR)));
	}

	@Test
	public void testColorLess() throws Exception {
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator comparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		assertEquals(-1, comparator.compare(buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FOUR), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FIVE)));
		assertEquals(-1, comparator.compare(buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FOUR), buildCard(PokerCardColor.HEARTS, PokerCardValue.FOUR)));
	}

	@Test
	public void testColorGreater() throws Exception {
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator comparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		assertEquals(1, comparator.compare(buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FIVE), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FOUR)));
		assertEquals(1, comparator.compare(buildCard(PokerCardColor.HEARTS, PokerCardValue.FOUR), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.FOUR)));
	}

	private PokerCardIdentifier buildCard(final PokerCardColor color, final PokerCardValue value) {
		return new PokerCardIdentifier(color, value);
	}
}
