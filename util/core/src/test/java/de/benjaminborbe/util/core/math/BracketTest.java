package de.benjaminborbe.util.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BracketTest {

	@Test
	public void testValue() throws Exception {
		final Bracket addition = new Bracket(new NumberValue("13.37"));
		assertEquals(13.37d, addition.getValue(), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		final Bracket addition = new Bracket(new NumberValue("13.37"));
		assertEquals("(13.37)", addition.asString());
	}
}
