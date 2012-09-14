package de.benjaminborbe.util.math.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.util.math.NumberValue;

import de.benjaminborbe.util.math.function.Max;

public class MaxTest {

	@Test
	public void testValue() throws Exception {
		{
			final Max max = new Max(new NumberValue("42"));
			assertEquals(42d, max.getValue(), 0d);
		}
		{
			final Max max = new Max(new NumberValue("42"), new NumberValue("21"));
			assertEquals(42d, max.getValue(), 0d);
		}
		{
			final Max max = new Max(new NumberValue("42"), new NumberValue("21"), new NumberValue("1337"));
			assertEquals(1337d, max.getValue(), 0d);
		}
	}

	@Test
	public void testAsString() throws Exception {
		{
			final Max max = new Max(new NumberValue("42"));
			assertEquals("max(42)", max.asString());
		}
		{
			final Max max = new Max(new NumberValue("42"), new NumberValue("21"));
			assertEquals("max(42,21)", max.asString());
		}
		{
			final Max max = new Max(new NumberValue("42"), new NumberValue("21"), new NumberValue("1337"));
			assertEquals("max(42,21,1337)", max.asString());
		}
	}
}
