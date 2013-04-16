package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
