package de.benjaminborbe.tools.lua;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LuaHash implements Lua {

	private final Map<String, Lua> data = new HashMap<String, Lua>();

	public Collection<String> keys() {
		return data.keySet();
	}

	public Lua get(final String string) {
		return data.get(string);
	}

	public void add(final String key, final Lua value) {
		data.put(key, value);
	}

	@Override
	public boolean isValue() {
		return false;
	}

	@Override
	public boolean isHash() {
		return true;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	@Override
	public String toString() {
		final StringWriter sw = new StringWriter();
		sw.append("{");
		for (final Entry<String, Lua> e : data.entrySet()) {
			sw.append(e.getKey());
			sw.append(" = ");
			sw.append(String.valueOf(e.getValue()));
			sw.append("\n");
		}
		sw.append("}\n");
		return sw.toString();
	}

}
