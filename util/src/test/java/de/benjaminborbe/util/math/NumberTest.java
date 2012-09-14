package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberTest {

	@Test
	public void testValue() throws Exception {
		{
			final Number number = new Number("42");
			assertEquals(42d, number.getValue(), 0d);
		}
		{
			final Number number = new Number("42.0");
			assertEquals(42d, number.getValue(), 0d);
		}
		{
			final Number number = new Number("42.00");
			assertEquals(42d, number.getValue(), 0d);
		}
		{
			final Number number = new Number("13.37");
			assertEquals(13.37d, number.getValue(), 0d);
		}
	}

	@Test
	public void testAsString() throws Exception {
		{
			final Number number = new Number("13.37");
			assertEquals("13.37", number.asString());
		}
		{
			final Number number = new Number("42");
			assertEquals("42", number.asString());
		}
	}

}
