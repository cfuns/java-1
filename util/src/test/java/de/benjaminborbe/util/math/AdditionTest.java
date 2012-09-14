package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdditionTest {

	@Test
	public void testValue() throws Exception {
		final Addition addition = new Addition(new Number("13.37"), new Number("42"));
		assertEquals(55.37d, addition.getValue(), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		final Addition addition = new Addition(new Number("13.37"), new Number("42"));
		assertEquals("13.37 + 42", addition.asString());
	}
}
