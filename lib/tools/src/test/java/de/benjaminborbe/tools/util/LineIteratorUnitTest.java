package de.benjaminborbe.tools.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LineIteratorUnitTest {

	@Test
	public void testIterator() throws Exception {
		{
			final LineIterator i = new LineIterator(null);
			assertFalse(i.hasNext());
		}
		{
			final LineIterator i = new LineIterator("");
			assertTrue(i.hasNext());
			assertEquals("", i.next());
			assertFalse(i.hasNext());
		}
		{
			final LineIterator i = new LineIterator(" ");
			assertTrue(i.hasNext());
			assertEquals(" ", i.next());
			assertFalse(i.hasNext());
		}
		{
			final LineIterator i = new LineIterator("\n");
			assertTrue(i.hasNext());
			assertEquals("", i.next());
			assertTrue(i.hasNext());
			assertEquals("", i.next());
			assertFalse(i.hasNext());
		}
		{
			final LineIterator i = new LineIterator("a\nb");
			assertTrue(i.hasNext());
			assertEquals("a", i.next());
			assertTrue(i.hasNext());
			assertEquals("b", i.next());
			assertFalse(i.hasNext());
		}
	}

}
