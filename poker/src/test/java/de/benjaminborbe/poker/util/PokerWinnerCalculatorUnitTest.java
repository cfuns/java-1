package de.benjaminborbe.poker.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.tools.util.ComparatorUtil;

public class PokerWinnerCalculatorUnitTest {

	@Test
	public void testCompareHighcard() throws Exception {
		final PokerWinnerCalculator comparator = getComparator();
		{
			final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = new HashMap<PokerPlayerIdentifier, Collection<PokerCardIdentifier>>();
			final Collection<PokerPlayerIdentifier> result = comparator.getWinners(playerCards);
			assertNotNull(result);
			assertEquals(0, result.size());
		}
		{
			final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = new HashMap<PokerPlayerIdentifier, Collection<PokerCardIdentifier>>();
			playerCards.put(new PokerPlayerIdentifier("playerA"), buildCards());
			final Collection<PokerPlayerIdentifier> result = comparator.getWinners(playerCards);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
		{
			final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = new HashMap<PokerPlayerIdentifier, Collection<PokerCardIdentifier>>();
			playerCards.put(new PokerPlayerIdentifier("playerA"), buildCards());
			playerCards.put(new PokerPlayerIdentifier("playerB"), buildCards());
			final Collection<PokerPlayerIdentifier> result = comparator.getWinners(playerCards);
			assertNotNull(result);
			assertEquals(2, result.size());
		}
		{
			final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = new HashMap<PokerPlayerIdentifier, Collection<PokerCardIdentifier>>();
			playerCards.put(new PokerPlayerIdentifier("playerA"), buildCards(PokerCardValue.ACE));
			playerCards.put(new PokerPlayerIdentifier("playerB"), buildCards(PokerCardValue.KING));
			final Collection<PokerPlayerIdentifier> result = comparator.getWinners(playerCards);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
		{
			final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = new HashMap<PokerPlayerIdentifier, Collection<PokerCardIdentifier>>();
			playerCards.put(new PokerPlayerIdentifier("playerA"), buildCards(PokerCardValue.ACE, PokerCardValue.TWO));
			playerCards.put(new PokerPlayerIdentifier("playerB"), buildCards(PokerCardValue.ACE, PokerCardValue.KING));
			final Collection<PokerPlayerIdentifier> result = comparator.getWinners(playerCards);
			assertNotNull(result);
			assertEquals(1, result.size());
		}
	}

	private PokerWinnerCalculator getComparator() {
		final ComparatorUtil comparatorUtil = new ComparatorUtil();
		final PokerValueComparator pokerValueComparator = new PokerValueComparator();
		final PokerColorComparator pokerColorComparator = new PokerColorComparator();
		final PokerCardComparator pokerCardComparator = new PokerCardComparator(pokerValueComparator, pokerColorComparator);
		final PokerCardUtil pokerCardUtil = new PokerCardUtil(comparatorUtil, pokerValueComparator);
		final PokerCardsHighcardComparator pokerCardsHighcardComparator = new PokerCardsHighcardComparator(comparatorUtil, pokerCardComparator);
		final PokerCardsRoyalFlushComparator pokerCardsRoyalFlushComparator = new PokerCardsRoyalFlushComparator(comparatorUtil, pokerCardUtil, pokerCardComparator);
		final PokerCardsStraightFlushComparator pokerCardsStraightFlushComparator = new PokerCardsStraightFlushComparator(comparatorUtil, pokerCardUtil, pokerCardComparator);
		final PokerCardsFourOfAKindComparator pokerCardsFourOfAKindComparator = new PokerCardsFourOfAKindComparator(pokerCardsHighcardComparator);
		final PokerCardsFullHouseComparator pokerCardsFullHouseComparator = new PokerCardsFullHouseComparator(comparatorUtil, pokerValueComparator);
		final PokerCardsFlushComparator pokerCardsFlushComparator = new PokerCardsFlushComparator(pokerCardUtil, comparatorUtil, pokerCardComparator);
		final PokerCardsStraightComparator pokerCardsStraightComparator = new PokerCardsStraightComparator(pokerCardUtil, pokerValueComparator);
		final PokerCardsTwoPairComparator pokerCardsTwoPairComparator = new PokerCardsTwoPairComparator(pokerCardsHighcardComparator);
		final PokerCardsThreeOfAKindComparator pokerCardsThreeOfAKindComparator = new PokerCardsThreeOfAKindComparator(pokerCardsHighcardComparator);
		final PokerCardsPairComparator pokerCardsPairComparator = new PokerCardsPairComparator(pokerCardsHighcardComparator);

		final PokerCardsComparator comparator = new PokerCardsComparator(pokerCardsRoyalFlushComparator, pokerCardsStraightFlushComparator, pokerCardsFourOfAKindComparator,
				pokerCardsFullHouseComparator, pokerCardsFlushComparator, pokerCardsStraightComparator, pokerCardsThreeOfAKindComparator, pokerCardsTwoPairComparator,
				pokerCardsPairComparator, pokerCardsHighcardComparator);
		return new PokerWinnerCalculator(comparator);
	}

	private Collection<PokerCardIdentifier> buildCards(final PokerCardValue... values) {
		final List<PokerCardIdentifier> result = new ArrayList<PokerCardIdentifier>();
		for (final PokerCardValue value : values) {
			result.add(new PokerCardIdentifier(PokerCardColor.CLUBS, value));
		}
		return result;
	}
}
