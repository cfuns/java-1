package de.benjaminborbe.poker.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.poker.api.PokerCardColor;

public class PokerCardColorComparatorUnitTest {

	@Test
	public void testColorEquals() throws Exception {
		final PokerColorComparator comparator = new PokerColorComparator();
		assertEquals(0, comparator.compare(PokerCardColor.CLUBS, PokerCardColor.CLUBS));
		assertEquals(0, comparator.compare(PokerCardColor.DIAMONDS, PokerCardColor.DIAMONDS));
		assertEquals(0, comparator.compare(PokerCardColor.HEARTS, PokerCardColor.HEARTS));
		assertEquals(0, comparator.compare(PokerCardColor.SPADES, PokerCardColor.SPADES));
	}

	@Test
	public void testColorLess() throws Exception {
		final PokerColorComparator comparator = new PokerColorComparator();
		assertEquals(-1, comparator.compare(PokerCardColor.DIAMONDS, PokerCardColor.CLUBS));
		assertEquals(-1, comparator.compare(PokerCardColor.HEARTS, PokerCardColor.CLUBS));
		assertEquals(-1, comparator.compare(PokerCardColor.SPADES, PokerCardColor.CLUBS));
	}

	@Test
	public void testColorGreater() throws Exception {
		final PokerColorComparator comparator = new PokerColorComparator();
		assertEquals(1, comparator.compare(PokerCardColor.CLUBS, PokerCardColor.DIAMONDS));
		assertEquals(1, comparator.compare(PokerCardColor.HEARTS, PokerCardColor.DIAMONDS));
		assertEquals(1, comparator.compare(PokerCardColor.SPADES, PokerCardColor.DIAMONDS));
	}
}
