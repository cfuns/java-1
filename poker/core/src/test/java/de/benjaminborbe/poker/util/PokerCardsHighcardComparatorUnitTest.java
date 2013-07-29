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

public class PokerCardsHighcardComparatorUnitTest {

	@Test
	public void testCompareHighcard() throws Exception {
		final PokerCardsHighcardComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

			buildCards(),

			buildCards()

		));

		assertEquals(0, comparator.compare(

			buildCards(PokerCardValue.ACE),

			buildCards(PokerCardValue.ACE)

		));

		assertEquals(-1, comparator.compare(

			buildCards(),

			buildCards(PokerCardValue.ACE)

		));

		assertEquals(1, comparator.compare(buildCards(PokerCardValue.ACE), buildCards()));

		assertEquals(-1, comparator.compare(

			buildCards(PokerCardValue.KING),

			buildCards(PokerCardValue.ACE)

		));

		assertEquals(1, comparator.compare(

			buildCards(PokerCardValue.ACE),

			buildCards(PokerCardValue.KING)

		));

		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.KING), buildCards(PokerCardValue.ACE, PokerCardValue.KING)));
		assertEquals(-1, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.QUEEN), buildCards(PokerCardValue.ACE, PokerCardValue.KING)));
		assertEquals(1, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.KING), buildCards(PokerCardValue.ACE, PokerCardValue.QUEEN)));

		assertEquals(-1, comparator.compare(buildCards(PokerCardValue.ACE), buildCards(PokerCardValue.ACE, PokerCardValue.KING)));
		assertEquals(1, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.KING), buildCards(PokerCardValue.ACE)));
	}

	private PokerCardsHighcardComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		return new PokerCardsHighcardComparator(comparatorUtil, pokerCardComparator);
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardValue... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardValue value : values) {
			result.add(new PokerCardIdentifier(PokerCardColor.CLUBS, value));
		}
		return result;
	}

}
