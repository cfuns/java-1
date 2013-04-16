package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
