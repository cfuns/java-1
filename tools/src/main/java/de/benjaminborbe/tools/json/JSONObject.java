package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface JSONObject extends JSONElement {

	JSONObject put(String key, Object value);

	void writeJSONString(Writer out) throws IOException;

	Object get(String key);

	JSONObject putAll(Map<String, ? extends Object> data);

	Set<Entry<String, Object>> entrySet();

	int size();

	boolean containsKey(String name);
}
