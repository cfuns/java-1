package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;
import java.util.Collection;

public class PokerCardsComparator extends ComparatorChain<Collection<PokerCardIdentifier>> {

	@SuppressWarnings("unchecked")
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
		final PokerCardsHighcardComparator pokerCardsHighcardComparator
	) {
		super(pokerCardsRoyalFlushComparator, pokerCardsStraightFlushComparator, pokerCardsFourOfAKindComparator, pokerCardsFullHouseComparator, pokerCardsFlushComparator,
			pokerCardsStraightComparator, pokerCardsThreeOfAKindComparator, pokerCardsTwoPairComparator, pokerCardsPairComparator, pokerCardsHighcardComparator);
	}

}
