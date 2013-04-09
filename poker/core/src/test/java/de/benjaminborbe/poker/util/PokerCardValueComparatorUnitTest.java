package de.benjaminborbe.poker.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.poker.api.PokerCardValue;

public class PokerCardValueComparatorUnitTest {

	@Test
	public void testValueEquals() throws Exception {
		final PokerValueComparator comparator = new PokerValueComparator();
		assertEquals(0, comparator.compare(PokerCardValue.TWO, PokerCardValue.TWO));
		assertEquals(0, comparator.compare(PokerCardValue.THREE, PokerCardValue.THREE));
		assertEquals(0, comparator.compare(PokerCardValue.FOUR, PokerCardValue.FOUR));
		assertEquals(0, comparator.compare(PokerCardValue.FIVE, PokerCardValue.FIVE));
		assertEquals(0, comparator.compare(PokerCardValue.SIX, PokerCardValue.SIX));
		assertEquals(0, comparator.compare(PokerCardValue.SEVEN, PokerCardValue.SEVEN));
		assertEquals(0, comparator.compare(PokerCardValue.EIGHT, PokerCardValue.EIGHT));
		assertEquals(0, comparator.compare(PokerCardValue.NINE, PokerCardValue.NINE));
		assertEquals(0, comparator.compare(PokerCardValue.TEN, PokerCardValue.TEN));
		assertEquals(0, comparator.compare(PokerCardValue.JACK, PokerCardValue.JACK));
		assertEquals(0, comparator.compare(PokerCardValue.QUEEN, PokerCardValue.QUEEN));
		assertEquals(0, comparator.compare(PokerCardValue.KING, PokerCardValue.KING));
		assertEquals(0, comparator.compare(PokerCardValue.ACE, PokerCardValue.ACE));
	}

	@Test
	public void testValueLess() throws Exception {

		final PokerValueComparator comparator = new PokerValueComparator();
		assertEquals(-1, comparator.compare(PokerCardValue.TWO, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.THREE, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.FOUR, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.FIVE, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.SIX, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.SEVEN, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.EIGHT, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.NINE, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.TEN, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.JACK, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.QUEEN, PokerCardValue.ACE));
		assertEquals(-1, comparator.compare(PokerCardValue.KING, PokerCardValue.ACE));
	}

	@Test
	public void testValueGreater() throws Exception {
		final PokerValueComparator comparator = new PokerValueComparator();
		assertEquals(1, comparator.compare(PokerCardValue.THREE, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.FOUR, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.FIVE, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.SIX, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.SEVEN, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.EIGHT, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.NINE, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.TEN, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.JACK, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.QUEEN, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.KING, PokerCardValue.TWO));
		assertEquals(1, comparator.compare(PokerCardValue.ACE, PokerCardValue.TWO));
	}
}
