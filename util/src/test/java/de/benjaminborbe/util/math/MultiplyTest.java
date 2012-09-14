package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultiplyTest {

	@Test
	public void testValue() throws Exception {
		final Multiply multiply = new Multiply(new Number("13.37"), new Number("2"));
		assertEquals(26.74d, multiply.getValue(), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		final Multiply multiply = new Multiply(new Number("13.37"), new Number("42"));
		assertEquals("13.37 * 42", multiply.asString());
	}
}
