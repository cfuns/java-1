package de.benjaminborbe.tools.json;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class JSONObjectSimpleUnitTest {

	@Test
	public void testValue() throws Exception {
		final JSONObject value = new JSONObjectSimple();

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("{}", sw.toString());
		}

		value.put("key1", "value1");

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("{\"key1\":\"value1\"}", sw.toString());
		}

		value.put("key2", "value2");

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("{\"key2\":\"value2\",\"key1\":\"value1\"}", sw.toString());
		}

		value.put("key3", null);

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("{\"key3\":null,\"key2\":\"value2\",\"key1\":\"value1\"}", sw.toString());
		}

	}

	@Test
	public void testJSONValue() throws IOException {
		final JSONObject value = new JSONObjectSimple();
		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("{}", sw.toString());
		}

		value.put("key1", new JSONArraySimple());

		{
			final StringWriter sw = new StringWriter();
			value.writeJSONString(sw);
			assertEquals("{\"key1\":[]}", sw.toString());
		}

	}
}
