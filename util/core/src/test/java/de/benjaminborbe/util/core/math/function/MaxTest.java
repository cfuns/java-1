package de.benjaminborbe.util.core.math.function;

import de.benjaminborbe.util.core.math.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxTest {

	@Test
	public void testValue() throws Exception {
		{
			assertEquals(42d, new Max().calucate(new NumberValue("42")), 0d);
		}
		{
			assertEquals(42d, new Max().calucate(new NumberValue("42"), new NumberValue("21")), 0d);
		}
		{
			assertEquals(1337d, new Max().calucate(new NumberValue("42"), new NumberValue("21"), new NumberValue("1337")), 0d);
		}
	}

	@Test
	public void testAsString() throws Exception {
		{
			assertEquals("max(42)", new Max().asString(new NumberValue("42")));
		}
		{
			assertEquals("max(42,21)", new Max().asString(new NumberValue("42"), new NumberValue("21")));
		}
		{
			assertEquals("max(42,21,1337)", new Max().asString(new NumberValue("42"), new NumberValue("21"), new NumberValue("1337")));
		}
	}
}
