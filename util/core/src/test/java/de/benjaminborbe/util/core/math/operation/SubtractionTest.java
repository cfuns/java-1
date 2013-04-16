package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubtractionTest {

	@Test
	public void testValue() throws Exception {
		assertEquals(11.37d, new Subtraction().calucate(new NumberValue("13.37"), new NumberValue("2")), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		assertEquals("13.37 - 42", new Subtraction().asString(new NumberValue("13.37"), new NumberValue("42")));
	}
}
