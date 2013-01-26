package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONArraySimple implements JSONArray {

	private final List<Object> values = new ArrayList<Object>();

	@Override
	public JSONArray add(final Object value) {
		this.values.add(value);
		return this;
	}

	@Override
	public void writeJSONString(final Writer out) throws IOException {
		writeJSONString(values, out);
	}

	public static void writeJSONString(final List<Object> values, final Writer out) throws IOException {
		if (values == null) {
			out.append("null");
			return;
		}
		out.append('[');
		boolean first = true;
		for (final Object value : values) {
			if (first) {
				first = false;
			}
			else {
				out.append(',');
			}
			JSONValueSimple.writeJSONString(value, out);
		}
		out.append(']');
	}

	@Override
	public Iterator<Object> iterator() {
		return values.iterator();
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public Object get(final int i) {
		return values.get(i);
	}

}
