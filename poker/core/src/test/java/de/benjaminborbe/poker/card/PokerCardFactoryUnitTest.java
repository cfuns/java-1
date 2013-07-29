package de.benjaminborbe.poker.card;

import de.benjaminborbe.poker.api.PokerCardIdentifier;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PokerCardFactoryUnitTest {

	@Test
	public void testCards() throws Exception {
		final PokerCardFactory pokerCardFactory = new PokerCardFactory();
		assertNotNull(pokerCardFactory.getCards());
		assertEquals(52, pokerCardFactory.getCards().size());
		final Set<PokerCardIdentifier> cards = new HashSet<PokerCardIdentifier>();
		for (final PokerCardIdentifier card : pokerCardFactory.getCards()) {
			cards.add(card);
		}
		assertEquals(52, cards.size());
	}
}
