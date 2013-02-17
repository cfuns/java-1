package de.benjaminborbe.poker.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class PokerCardsFullHouseComparatorUnitTest {

	@Test
	public void testCompareNoCard() throws Exception {
		final PokerCardsFullHouseComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(), buildCards()));
	}

	@Test
	public void testCompareOneCard() throws Exception {
		final PokerCardsFullHouseComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE),

		buildCards(PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareTwoCards() throws Exception {
		final PokerCardsFullHouseComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE),

		buildCards(PokerCardValue.KING, PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareThreeCards() throws Exception {
		final PokerCardsFullHouseComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE),

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareFourCards() throws Exception {
		final PokerCardsFullHouseComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE),

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareFiveCard() throws Exception {
		final PokerCardsFullHouseComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.TWO, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO),

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.TWO, PokerCardValue.TWO)

		));

	}

	private PokerCardsFullHouseComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		return new PokerCardsFullHouseComparator(comparatorUtil, pokerValueComparator);
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardValue... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardValue value : values) {
			result.add(new PokerCardIdentifier(PokerCardColor.CLUBS, value));
		}
		return result;
	}
}
