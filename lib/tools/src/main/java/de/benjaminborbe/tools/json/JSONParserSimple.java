package de.benjaminborbe.tools.json;

import javax.inject.Inject;

public class JSONParserSimple implements JSONParser {

	@Inject
	public JSONParserSimple() {
	}

	@Override
	public Object parse(final String jsonString) throws JSONParseException {
		final org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
		try {
			final Object object = parser.parse(jsonString);
			return handle(object);
		}
		catch (final org.json.simple.parser.ParseException e) {
			throw new JSONParseException(e);
		}
	}

	private Object handle(final Object object) {
		if (object instanceof org.json.simple.JSONArray) {
			return handle((org.json.simple.JSONArray) object);
		}
		if (object instanceof org.json.simple.JSONObject) {
			return handle((org.json.simple.JSONObject) object);
		}
		return object;
	}

	private JSONObject handle(final org.json.simple.JSONObject object) {
		final JSONObject jsonObject = new JSONObjectSimple();
		for (final Object key : object.keySet()) {
			final Object value = object.get(key);
			jsonObject.put(String.valueOf(key), handle(value));
		}
		return jsonObject;
	}

	private JSONArray handle(final org.json.simple.JSONArray object) {
		final JSONArray result = new JSONArraySimple();
		for (final Object e : object) {
			result.add(handle(e));
		}
		return result;
	}
}
