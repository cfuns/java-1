package de.benjaminborbe.util.core.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumberValueTest {

	@Test
	public void testValue() throws Exception {
		{
			final NumberValue number = new NumberValue("42");
			assertEquals(42d, number.getValue(), 0d);
		}
		{
			final NumberValue number = new NumberValue("42.0");
			assertEquals(42d, number.getValue(), 0d);
		}
		{
			final NumberValue number = new NumberValue("42.00");
			assertEquals(42d, number.getValue(), 0d);
		}
		{
			final NumberValue number = new NumberValue("13.37");
			assertEquals(13.37d, number.getValue(), 0d);
		}
	}

	@Test
	public void testAsString() throws Exception {
		{
			final NumberValue number = new NumberValue("13.37");
			assertEquals("13.37", number.asString());
		}
		{
			final NumberValue number = new NumberValue("42");
			assertEquals("42", number.asString());
		}
	}

}
