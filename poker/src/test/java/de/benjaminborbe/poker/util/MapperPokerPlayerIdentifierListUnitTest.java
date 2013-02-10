package de.benjaminborbe.poker.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

public class MapperPokerPlayerIdentifierListUnitTest {

	@Test
	public void testToString() throws Exception {
		final MapperPokerPlayerIdentifierList mapper = new MapperPokerPlayerIdentifierList(new MapperPokerPlayerIdentifier());
		assertEquals(null, mapper.toString(new ArrayList<PokerPlayerIdentifier>()));
		assertEquals("1337", mapper.toString(Arrays.asList(new PokerPlayerIdentifier("1337"))));
		assertEquals("1337,42", mapper.toString(Arrays.asList(new PokerPlayerIdentifier("1337"), new PokerPlayerIdentifier("42"))));
	}

	@Test
	public void testFromtring() throws Exception {
		final MapperPokerPlayerIdentifierList mapper = new MapperPokerPlayerIdentifierList(new MapperPokerPlayerIdentifier());
		{
			final List<PokerPlayerIdentifier> list = mapper.fromString(null);
			assertNotNull(list);
			assertEquals(0, list.size());
		}
		{
			final List<PokerPlayerIdentifier> list = mapper.fromString("42");
			assertNotNull(list);
			assertEquals(1, list.size());
			assertEquals("42", list.get(0).getId());
		}
		{
			final List<PokerPlayerIdentifier> list = mapper.fromString("42,1337");
			assertNotNull(list);
			assertEquals(2, list.size());
			assertEquals("42", list.get(0).getId());
			assertEquals("1337", list.get(1).getId());
		}
	}
}
