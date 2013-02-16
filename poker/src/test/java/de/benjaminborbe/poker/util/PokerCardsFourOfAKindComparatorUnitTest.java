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

public class PokerCardsFourOfAKindComparatorUnitTest {

	@Test
	public void testCompareFourCard() throws Exception {
		final PokerCardsFourOfAKindComparator comparator = getComparator();

		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO, PokerCardValue.TWO)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE),

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.KING),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.ACE)

		));

	}

	private PokerCardsFourOfAKindComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		final PokerCardsHighcardComparator pokerCardsHighcardComparator = new PokerCardsHighcardComparator(comparatorUtil, pokerCardComparator);
		final PokerCardsFourOfAKindComparator comparator = new PokerCardsFourOfAKindComparator(pokerCardsHighcardComparator);
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
