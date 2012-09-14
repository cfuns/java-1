package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DivisionTest {

	@Test
	public void testValue() throws Exception {
		final Division division = new Division(new Number("42"), new Number("2"));
		assertEquals(21d, division.getValue(), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		final Division division = new Division(new Number("13.37"), new Number("42"));
		assertEquals("13.37 / 42", division.asString());
	}
}
