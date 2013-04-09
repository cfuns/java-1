package de.benjaminborbe.util.math.operation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.util.math.NumberValue;

public class AdditionTest {

	@Test
	public void testValue() throws Exception {
		assertEquals(55.37d, new Addition().calucate(new NumberValue("13.37"), new NumberValue("42")), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		assertEquals("13.37 + 42", new Addition().asString(new NumberValue("13.37"), new NumberValue("42")));
	}
}
