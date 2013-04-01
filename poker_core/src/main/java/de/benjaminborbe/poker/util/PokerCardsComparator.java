package de.benjaminborbe.poker.util;

import java.util.Collection;

import com.google.inject.Inject;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.tools.util.ComparatorChain;

public class PokerCardsComparator extends ComparatorChain<Collection<PokerCardIdentifier>> {

	@Inject
	public PokerCardsComparator(
			final PokerCardsRoyalFlushComparator pokerCardsRoyalFlushComparator,
			final PokerCardsStraightFlushComparator pokerCardsStraightFlushComparator,
			final PokerCardsFourOfAKindComparator pokerCardsFourOfAKindComparator,
			final PokerCardsFullHouseComparator pokerCardsFullHouseComparator,
			final PokerCardsFlushComparator pokerCardsFlushComparator,
			final PokerCardsStraightComparator pokerCardsStraightComparator,
			final PokerCardsThreeOfAKindComparator pokerCardsThreeOfAKindComparator,
			final PokerCardsTwoPairComparator pokerCardsTwoPairComparator,
			final PokerCardsPairComparator pokerCardsPairComparator,
			final PokerCardsHighcardComparator pokerCardsHighcardComparator) {
		super(pokerCardsRoyalFlushComparator, pokerCardsStraightFlushComparator, pokerCardsFourOfAKindComparator, pokerCardsFullHouseComparator, pokerCardsFlushComparator,
				pokerCardsStraightComparator, pokerCardsThreeOfAKindComparator, pokerCardsTwoPairComparator, pokerCardsPairComparator, pokerCardsHighcardComparator);
	}

}
