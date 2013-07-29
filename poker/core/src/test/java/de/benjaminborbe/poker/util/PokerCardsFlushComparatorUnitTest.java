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

public class PokerCardsFlushComparatorUnitTest {

	@Test
	public void testCompareNoCard() throws Exception {
		final PokerCardsFlushComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(),

			buildCards()

		));
	}

	@Test
	public void testCompareOneCard() throws Exception {
		final PokerCardsFlushComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE)),

			buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.KING))

		));
	}

	@Test
	public void testCompareTwoCard() throws Exception {
		final PokerCardsFlushComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.ACE)),

			buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.KING), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING))

		));
	}

	@Test
	public void testCompareThreeCard() throws Exception {
		final PokerCardsFlushComparator comparator = getComparator();
		assertEquals(
			0,
			comparator.compare(

				buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.ACE),
					buildCard(PokerCardColor.HEARTS, PokerCardValue.ACE)),

				buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.KING), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING),
					buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING))

			));
	}

	@Test
	public void testCompareFourCard() throws Exception {
		final PokerCardsFlushComparator comparator = getComparator();
		assertEquals(
			0,
			comparator.compare(

				buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.ACE),
					buildCard(PokerCardColor.HEARTS, PokerCardValue.ACE), buildCard(PokerCardColor.SPADES, PokerCardValue.ACE)),

				buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.KING), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING),
					buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING), buildCard(PokerCardColor.SPADES, PokerCardValue.KING))

			));
	}

	@Test
	public void testCompareFiveCard() throws Exception {
		final PokerCardsFlushComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.ACE),
				buildCard(PokerCardColor.HEARTS, PokerCardValue.ACE), buildCard(PokerCardColor.SPADES, PokerCardValue.ACE), buildCard(PokerCardColor.SPADES, PokerCardValue.QUEEN)),

			buildCards(buildCard(PokerCardColor.CLUBS, PokerCardValue.KING), buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING),
				buildCard(PokerCardColor.DIAMONDS, PokerCardValue.KING), buildCard(PokerCardColor.SPADES, PokerCardValue.KING),
				buildCard(PokerCardColor.DIAMONDS, PokerCardValue.QUEEN))

		));

		assertEquals(-1, comparator.compare(buildCards(

			buildCard(PokerCardColor.CLUBS, PokerCardValue.KING),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.QUEEN),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.JACK),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.TEN),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.NINE)

		), buildCards(

			buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.KING),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.QUEEN),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.JACK),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.TEN)

		)));

		assertEquals(1, comparator.compare(buildCards(

			buildCard(PokerCardColor.CLUBS, PokerCardValue.ACE),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.KING),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.QUEEN),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.JACK),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.TEN)

		), buildCards(

			buildCard(PokerCardColor.CLUBS, PokerCardValue.KING),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.QUEEN),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.JACK),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.TEN),

			buildCard(PokerCardColor.CLUBS, PokerCardValue.NINE)

		)));

	}

	private PokerCardsFlushComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		final PokerCardUtil pokerCardUtil = new PokerCardUtil(comparatorUtil, pokerValueComparator);
		return new PokerCardsFlushComparator(pokerCardUtil, comparatorUtil, pokerCardComparator);
	}

	private PokerCardIdentifier buildCard(final PokerCardColor color, final PokerCardValue value) {
		return new PokerCardIdentifier(color, value);
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardIdentifier... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardIdentifier value : values) {
			result.add(value);
		}
		return result;
	}
}
