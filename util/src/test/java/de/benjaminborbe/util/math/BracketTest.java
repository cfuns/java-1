package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BracketTest {

	@Test
	public void testValue() throws Exception {
		final Bracket addition = new Bracket(new Number("13.37"));
		assertEquals(13.37d, addition.getValue(), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		final Bracket addition = new Bracket(new Number("13.37"));
		assertEquals("(13.37)", addition.asString());
	}
}
