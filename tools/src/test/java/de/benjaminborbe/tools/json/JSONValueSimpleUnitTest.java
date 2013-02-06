package de.benjaminborbe.tools.json;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Test;

public class JSONValueSimpleUnitTest {

	@Test
	public void testValue() throws Exception {
		{
			final JSONValue value = new JSONValueSimple("test");
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("\"test\"", sw.toString());
		}
		{
			final JSONValue value = new JSONValueSimple(1);
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("1", sw.toString());
		}
		{
			final JSONValue value = new JSONValueSimple(null);
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("null", sw.toString());
		}
		{
			final JSONValue value = new JSONValueSimple(new TestValue("1"));
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("\"1\"", sw.toString());
		}
	}
}
