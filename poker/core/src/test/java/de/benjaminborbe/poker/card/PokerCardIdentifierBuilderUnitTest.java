package de.benjaminborbe.poker.card;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;

public class PokerCardIdentifierBuilderUnitTest {

	@Test
	public void testBuild() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ParseUtil parseUtil = new ParseUtilImpl();
		final PokerCardIdentifierBuilder builder = new PokerCardIdentifierBuilder(logger, parseUtil);
		final PokerCardIdentifier id = new PokerCardIdentifier(PokerCardColor.SPADES, PokerCardValue.ACE);
		final PokerCardIdentifier newId = builder.buildIdentifier(id.getId());
		assertEquals(id, newId);
	}
}
