package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.poker.card.PokerCardIdentifierBuilder;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapperPokerCardIdentifierListUnitTest {

	@Test
	public void testToString() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final ParseUtil parseUtil = new ParseUtilImpl();
		final PokerCardIdentifierBuilder pokerCardIdentifierBuilder = new PokerCardIdentifierBuilder(logger, parseUtil);
		final MapperPokerCardIdentifier mapperPokerCardIdentifier = new MapperPokerCardIdentifier(pokerCardIdentifierBuilder);
		final MapperPokerCardIdentifierList mapper = new MapperPokerCardIdentifierList(mapperPokerCardIdentifier);
		assertEquals(null, mapper.toString(new ArrayList<PokerCardIdentifier>()));
		assertEquals("SPADES_TEN", mapper.toString(Arrays.asList(new PokerCardIdentifier(PokerCardColor.SPADES, PokerCardValue.TEN))));
		assertEquals("SPADES_TEN,HEARTS_ACE",
			mapper.toString(Arrays.asList(new PokerCardIdentifier(PokerCardColor.SPADES, PokerCardValue.TEN), new PokerCardIdentifier(PokerCardColor.HEARTS, PokerCardValue.ACE))));
	}

	@Test
	public void testFromtring() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final ParseUtil parseUtil = new ParseUtilImpl();
		final PokerCardIdentifierBuilder pokerCardIdentifierBuilder = new PokerCardIdentifierBuilder(logger, parseUtil);
		final MapperPokerCardIdentifier mapperPokerCardIdentifier = new MapperPokerCardIdentifier(pokerCardIdentifierBuilder);
		final MapperPokerCardIdentifierList mapper = new MapperPokerCardIdentifierList(mapperPokerCardIdentifier);
		{
			final List<PokerCardIdentifier> list = mapper.fromString(null);
			assertNotNull(list);
			assertEquals(0, list.size());
		}
		{
			final List<PokerCardIdentifier> list = mapper.fromString("SPADES_TEN");
			assertNotNull(list);
			assertEquals(1, list.size());
			assertNotNull(list.get(0));
			assertEquals("SPADES_TEN", list.get(0).getId());
		}
		{
			final List<PokerCardIdentifier> list = mapper.fromString("SPADES_TEN,CLUBS_ACE");
			assertNotNull(list);
			assertEquals(2, list.size());
			assertNotNull(list.get(0));
			assertEquals("SPADES_TEN", list.get(0).getId());
			assertNotNull(list.get(1));
			assertEquals("CLUBS_ACE", list.get(1).getId());
		}
	}
}
