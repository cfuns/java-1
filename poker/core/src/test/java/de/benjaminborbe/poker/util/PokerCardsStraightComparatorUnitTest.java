package de.benjaminborbe.poker.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class PokerCardsStraightComparatorUnitTest {

	@Test
	public void testCompareNoCard() throws Exception {
		final PokerCardsStraightComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(), buildCards()));
	}

	@Test
	public void testCompareOneCard() throws Exception {
		final PokerCardsStraightComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE),

		buildCards(PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareTwoCards() throws Exception {
		final PokerCardsStraightComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.KING),

		buildCards(PokerCardValue.KING, PokerCardValue.QUEEN)

		));
	}

	@Test
	public void testCompareThreeCards() throws Exception {
		final PokerCardsStraightComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN),

		buildCards(PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK)

		));
	}

	@Test
	public void testCompareFourCards() throws Exception {
		final PokerCardsStraightComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK),

		buildCards(PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN)

		));
	}

	@Test
	public void testCompareFIVECards() throws Exception {
		final PokerCardsStraightComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN),

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN),

		buildCards(PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN, PokerCardValue.NINE)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN, PokerCardValue.NINE),

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN),

		buildCards(PokerCardValue.TWO, PokerCardValue.TWO, PokerCardValue.TWO, PokerCardValue.TWO, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.TWO, PokerCardValue.TWO, PokerCardValue.TWO, PokerCardValue.TWO, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK, PokerCardValue.TEN)

		));
	}

	private PokerCardsStraightComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerCardUtil pokerCardUtil = new PokerCardUtil(comparatorUtil, pokerValueComparator);
		return new PokerCardsStraightComparator(pokerCardUtil, pokerValueComparator);
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardValue... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardValue value : values) {
			result.add(new PokerCardIdentifier(PokerCardColor.CLUBS, value));
		}
		return result;
	}
}
