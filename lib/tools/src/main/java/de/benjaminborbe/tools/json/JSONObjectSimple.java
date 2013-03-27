package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObjectSimple implements JSONObject, Map<String, Object> {

	private final Map<String, Object> data = new HashMap<String, Object>();

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
	public Set<Entry<String, Object>> entrySet() {
		return data.entrySet();
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean containsKey(final Object key) {
		return data.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return data.containsValue(value);
	}

	@Override
	public Object get(final Object key) {
		return data.get(key);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return data.keySet();
	}

	@Override
	public Object remove(final Object key) {
		return data.remove(key);
	}

	@Override
	public Collection<Object> values() {
		return data.values();
	}

	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		data.putAll(m);
	}

	@Override
	public Object put(final String key, final Object value) {
		return data.put(key, value);
	}

}
