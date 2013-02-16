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

public class PokerCardsPairComparatorUnitTest {

	@Test
	public void testCompareNoCard() throws Exception {
		final PokerCardsPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(), buildCards()));
	}

	@Test
	public void testCompareOneCard() throws Exception {
		final PokerCardsPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE), buildCards(PokerCardValue.ACE)));
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE), buildCards(PokerCardValue.KING)));
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.KING), buildCards(PokerCardValue.ACE)));
	}

	@Test
	public void testCompareTwoCard() throws Exception {
		final PokerCardsPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.ACE), buildCards(PokerCardValue.ACE, PokerCardValue.ACE)));
		assertEquals(1, comparator.compare(buildCards(PokerCardValue.ACE, PokerCardValue.ACE), buildCards(PokerCardValue.KING, PokerCardValue.KING)));
		assertEquals(-1, comparator.compare(buildCards(PokerCardValue.KING, PokerCardValue.KING), buildCards(PokerCardValue.ACE, PokerCardValue.ACE)));
	}

	@Test
	public void testCompareThreeCard() throws Exception {
		final PokerCardsPairComparator comparator = getComparator();

		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO),

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO)

		));

		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE)

		));

		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.TWO)

		));

	}

	@Test
	public void testCompareFourCard() throws Exception {
		final PokerCardsPairComparator comparator = getComparator();
		assertEquals(0, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE, PokerCardValue.TWO)

		));
		assertEquals(1, comparator.compare(

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE, PokerCardValue.TWO),

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.THREE, PokerCardValue.TWO)

		));
		assertEquals(-1, comparator.compare(

		buildCards(PokerCardValue.KING, PokerCardValue.KING, PokerCardValue.THREE, PokerCardValue.TWO),

		buildCards(PokerCardValue.ACE, PokerCardValue.ACE, PokerCardValue.THREE, PokerCardValue.TWO)

		));

	}

	private PokerCardsPairComparator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		final PokerCardsHighcardComparator pokerCardsHighcardComparator = new PokerCardsHighcardComparator(comparatorUtil, pokerCardComparator);
		final PokerCardsPairComparator comparator = new PokerCardsPairComparator(pokerCardsHighcardComparator);
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
