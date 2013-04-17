package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MapperMapStringUnitTest {

	@Test
	public void testMapping() throws Exception {
		final JSONParser jsonParser = new JSONParserSimple();
		final MapperMapString mapper = new MapperMapString(jsonParser);
		final Map<String, String> data = new HashMap<>();
		data.put("keyA", "valueA");
		data.put("keyB", "valueB");
		data.put("keyC", "valueC");
		final String json = mapper.toString(data);
		assertNotNull(json);
		final Map<String, String> d = mapper.fromString(json);
		assertEquals("valueA", d.get("keyA"));
		assertEquals("valueB", d.get("keyB"));
		assertEquals("valueC", d.get("keyC"));
		assertEquals(data.size(), d.size());
	}

	@Test
	public void testMappingNull() throws Exception {
		final JSONParser jsonParser = new JSONParserSimple();
		final MapperMapString mapper = new MapperMapString(jsonParser);
		final Map<String, String> data = null;
		final String json = mapper.toString(data);
		assertNull(json);
		final Map<String, String> d = mapper.fromString(json);
		assertNull(d);
	}

	@Test
	public void testMappingEmpty() throws Exception {
		final JSONParser jsonParser = new JSONParserSimple();
		final MapperMapString mapper = new MapperMapString(jsonParser);
		final Map<String, String> data = new HashMap<>();
		final String json = mapper.toString(data);
		assertNotNull(json);
		final Map<String, String> d = mapper.fromString(json);
		assertEquals(data.size(), d.size());
	}

	@Test
	public void testMappingNullValue() throws Exception {
		final JSONParser jsonParser = new JSONParserSimple();
		final MapperMapString mapper = new MapperMapString(jsonParser);
		final Map<String, String> data = new HashMap<>();
		data.put("keyA", null);
		data.put("keyB", "valueB");
		data.put("keyC", "valueC");
		final String json = mapper.toString(data);
		assertNotNull(json);
		final Map<String, String> d = mapper.fromString(json);
		assertEquals(null, d.get("keyA"));
		assertEquals("valueB", d.get("keyB"));
		assertEquals("valueC", d.get("keyC"));
		assertEquals(data.size(), d.size());
	}
}
