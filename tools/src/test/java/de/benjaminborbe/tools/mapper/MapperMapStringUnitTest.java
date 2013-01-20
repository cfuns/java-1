package de.benjaminborbe.tools.mapper;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapperMapStringUnitTest {

	@Test
	public void testMapping() throws Exception {
		final MapperMapString mapper = new MapperMapString();
		final Map<String, String> data = new HashMap<String, String>();
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
}
