package de.benjaminborbe.tools.json;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JSONArraySimpleUnitTest {

	@Test
	public void testValue() throws Exception {
		final JSONArray value = new JSONArraySimple();

		value.add("value1");

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("[\"value1\"]", sw.toString());
		}
		value.add("value2");

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("[\"value1\",\"value2\"]", sw.toString());
		}
	}

	@Test
	public void testObject() throws Exception {
		final JSONArray value = new JSONArraySimple();

		value.add(new TestValue("value1"));

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("[\"value1\"]", sw.toString());
		}

		value.add(new TestValue("value2"));

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("[\"value1\",\"value2\"]", sw.toString());
		}
	}

}
