package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SubtractionTest {

	@Test
	public void testValue() throws Exception {
		final Subtraction subtraction = new Subtraction(new Number("13.37"), new Number("2"));
		assertEquals(11.37d, subtraction.getValue(), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		final Subtraction subtraction = new Subtraction(new Number("13.37"), new Number("42"));
		assertEquals("13.37 - 42", subtraction.asString());
	}
}
