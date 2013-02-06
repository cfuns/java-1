package de.benjaminborbe.tools.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSONParserSimpleUnitTest {

	@Test
	public void testParse() throws Exception {
		final JSONParser jsonParser = new JSONParserSimple();
		{
			final Object result = jsonParser.parse("null");
			assertNull(result);
		}
		{
			final Object result = jsonParser.parse("{}");
			assertNotNull(result);
			assertTrue(result instanceof JSONObject);
		}
		{
			final Object result = jsonParser.parse("[]");
			assertNotNull(result);
			assertTrue(result instanceof JSONArray);
		}
		{
			final Object result = jsonParser.parse("[\"a\",\"b\"]");
			assertNotNull(result);
			assertTrue(result instanceof JSONArray);
		}
	}
}
