package de.benjaminborbe.util.math.operation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.util.math.NumberValue;

public class DivisionTest {

	@Test
	public void testValue() throws Exception {
		assertEquals(21d, new Division().calucate(new NumberValue("42"), new NumberValue("2")), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		assertEquals("13.37 / 42", new Division().asString(new NumberValue("13.37"), new NumberValue("42")));
	}
}
