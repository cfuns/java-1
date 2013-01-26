package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JSONObjectSimple implements JSONObject {

	private final Map<String, Object> data = new HashMap<String, Object>();

	@Override
	public JSONObject put(final String key, final Object value) {
		data.put(key, value);
		return this;
	}

	@Override
	public void writeJSONString(final Writer out) throws IOException {
		writeJSONString(data, out);
	}

	public static void writeJSONString(final Map<String, Object> data, final Writer out) throws IOException {
		if (data == null) {
			out.append("null");
			return;
		}
		out.append('{');
		boolean first = true;
		for (final Entry<String, Object> e : data.entrySet()) {
			if (first) {
				first = false;
			}
			else {
				out.append(',');
			}
			final String key = e.getKey();
			final Object value = e.getValue();
			JSONValueSimple.writeJSONString(key, out);
			out.append(':');
			JSONValueSimple.writeJSONString(value, out);
		}
		out.append('}');
	}

	@Override
	public Object get(final String key) {
		return data.get(key);
	}

	@Override
	public JSONObject putAll(final Map<String, ? extends Object> data) {
		for (final Entry<String, ? extends Object> e : data.entrySet()) {
			put(e.getKey(), e.getValue());
		}
		return this;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return data.entrySet();
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean containsKey(final String key) {
		return data.containsKey(key);
	}

}
