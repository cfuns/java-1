package de.benjaminborbe.poker.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.poker.card.PokerCardIdentifierBuilder;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

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
		assertEquals("A_V10", mapper.toString(Arrays.asList(new PokerCardIdentifier(PokerCardColor.A, PokerCardValue.V10))));
		assertEquals("A_V10,B_VA",
				mapper.toString(Arrays.asList(new PokerCardIdentifier(PokerCardColor.A, PokerCardValue.V10), new PokerCardIdentifier(PokerCardColor.B, PokerCardValue.VA))));
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
			final List<PokerCardIdentifier> list = mapper.fromString("A_V10");
			assertNotNull(list);
			assertEquals(1, list.size());
			assertNotNull(list.get(0));
			assertEquals("A_V10", list.get(0).getId());
		}
		{
			final List<PokerCardIdentifier> list = mapper.fromString("A_V10,B_VA");
			assertNotNull(list);
			assertEquals(2, list.size());
			assertNotNull(list.get(0));
			assertEquals("A_V10", list.get(0).getId());
			assertNotNull(list.get(1));
			assertEquals("B_VA", list.get(1).getId());
		}
	}
}
