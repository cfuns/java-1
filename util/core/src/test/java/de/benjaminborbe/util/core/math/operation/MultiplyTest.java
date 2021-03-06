package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MultiplyTest {

	@Test
	public void testValue() throws Exception {
		assertEquals(26.74d, new Multiply().calucate(new NumberValue("13.37"), new NumberValue("2")), 0d);
	}

	@Test
	public void testAsString() throws Exception {
		assertEquals("13.37 * 42", new Multiply().asString(new NumberValue("13.37"), new NumberValue("42")));
	}
}
