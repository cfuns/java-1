package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;

public class JSONValueSimple implements JSONValue {

	private final Object value;

	public JSONValueSimple(final Object value) {
		this.value = value;
	}

	@Override
	public void writeJSONString(final Writer out) throws IOException {
		writeJSONString(value, out);
	}

	public static void writeJSONString(final Object value, final Writer out) throws IOException {
		if (value instanceof JSONArray) {
			final JSONArray jsonArray = (JSONArray) value;
			jsonArray.writeJSONString(out);
		}
		else if (value instanceof JSONObject) {
			final JSONObject jsonObject = (JSONObject) value;
			jsonObject.writeJSONString(out);
		}
		else {
			org.json.simple.JSONValue.writeJSONString(ecape(value), out);
		}
	}

	private static Object ecape(final Object value) {
		if (value instanceof Double || value instanceof Float || value instanceof Integer || value instanceof Long || value instanceof Boolean || value instanceof String) {
			return value;
		}
		else {
			return value != null ? String.valueOf(value) : null;
		}
	}
}
