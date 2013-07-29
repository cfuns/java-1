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

public class PokerCardsRoyalFlushComparatorUnitTest {

	@Test
	public void testCompareNoCard() throws Exception {
		final PokerCardsRoyalFlushComparator comparator = getPokerCardsRoyalFlushComparator();
		assertEquals(0, comparator.compare(buildCards(), buildCards()));
	}

	@Test
	public void testCompareOneCard() throws Exception {
		final PokerCardsRoyalFlushComparator comparator = getPokerCardsRoyalFlushComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE),

			buildCards(PokerCardValue.ACE)

		));
	}

	@Test
	public void testCompareTwoCards() throws Exception {
		final PokerCardsRoyalFlushComparator comparator = getPokerCardsRoyalFlushComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.KING),

			buildCards(PokerCardValue.ACE, PokerCardValue.KING)

		));
	}

	@Test
	public void testCompareThreeCards() throws Exception {
		final PokerCardsRoyalFlushComparator comparator = getPokerCardsRoyalFlushComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN),

			buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN)

		));
	}

	@Test
	public void testCompareFourCards() throws Exception {
		final PokerCardsRoyalFlushComparator comparator = getPokerCardsRoyalFlushComparator();
		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK),

			buildCards(PokerCardValue.ACE, PokerCardValue.KING, PokerCardValue.QUEEN, PokerCardValue.JACK)

		));
	}

	@Test
	public void testCompareFiveCards() throws Exception {
		final PokerCardsRoyalFlushComparator comparator = getPokerCardsRoyalFlushComparator();
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

	}

	private PokerCardsRoyalFlushComparator getPokerCardsRoyalFlushComparator() {
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerCardUtil pokerCardUtil = new PokerCardUtil(comparatorUtil, pokerValueComparator);
		return new PokerCardsRoyalFlushComparator(comparatorUtil, pokerCardUtil, pokerCardComparator);
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardValue... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardValue value : values) {
			result.add(new PokerCardIdentifier(PokerCardColor.CLUBS, value));
		}
		return result;
	}
}
