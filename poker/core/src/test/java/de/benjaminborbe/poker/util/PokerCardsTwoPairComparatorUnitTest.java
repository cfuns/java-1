package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.util.ComparatorUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PokerCardsTwoPairComparatorUnitTest {

	@Test
	public void testCompareNoCard() throws Exception {
		final PokerCardsTwoPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(), buildCards()));
	}

	@Test
	public void testCompareOneCard() throws Exception {
		final PokerCardsTwoPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE), buildCards(PokerCardValue.ACE)));
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE), buildCards(PokerCardValue.KING)));
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.KING), buildCards(PokerCardValue.ACE)));
	}

	@Test
	public void testCompareTwoCard() throws Exception {
		final PokerCardsTwoPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.ACE), buildCards(PokerCardValue.ACE, PokerCardValue.ACE)));
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.ACE), buildCards(PokerCardValue.KING, PokerCardValue.KING)));
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.KING, PokerCardValue.KING), buildCards(PokerCardValue.ACE, PokerCardValue.ACE)));
	}

	@Test
	public void testCompareThreeCard() throws Exception {
		final PokerCardsTwoPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE),

			buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareFourCard() throws Exception {
		final PokerCardsTwoPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE),

			buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING)

		));

		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO),

			buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.TWO, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.TWO, PokerCardValue.TWO),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE, PokerCardValue.THREE),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE, PokerCardValue.THREE)

		));

	}

	@Test
	public void testCompareFiveCard() throws Exception {
		final PokerCardsTwoPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.TWO, PokerCardValue.THREE, PokerCardValue.FOUR, PokerCardValue.FIVE, PokerCardValue.SIX),

			buildCards(PokerCardValue.SEVEN, PokerCardValue.EIGHT, PokerCardValue.NINE, PokerCardValue.TEN, PokerCardValue.JACK)

		));

		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.JACK),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.JACK)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.JACK, PokerCardValue.JACK, PokerCardValue.TWO),

			buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.JACK, PokerCardValue.JACK, PokerCardValue.THREE)

		));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.JACK, PokerCardValue.JACK, PokerCardValue.THREE),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.JACK, PokerCardValue.JACK, PokerCardValue.TWO)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.THREE),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.JACK, PokerCardValue.JACK, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.JACK, PokerCardValue.JACK, PokerCardValue.TWO),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.THREE)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.THREE),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.TWO),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.THREE)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.TWO),

			buildCards(PokerCardValue.TWO, PokerCardValue.THREE, PokerCardValue.FOUR, PokerCardValue.FIVE, PokerCardValue.SIX)

		));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.TWO, PokerCardValue.THREE, PokerCardValue.FOUR, PokerCardValue.FIVE, PokerCardValue.SIX),

			buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.QUEEN, PokerCardValue.QUEEN, PokerCardValue.TWO)

		));
	}

	private PokerCardsTwoPairComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		final PokerCardsHighcardComparator pokerCardsHighcardComparator = new PokerCardsHighcardComparator(comparatorUtil, pokerCardComparator);
		final PokerCardsTwoPairComparator comparator = new PokerCardsTwoPairComparator(pokerCardsHighcardComparator);
		return comparator;
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardValue... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardValue value : values) {
			result.add(new PokerCardIdentifier(PokerCardColor.CLUBS, value));
		}
		return result;
	}
}
