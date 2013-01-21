package de.benjaminborbe.tools.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapperMapString implements Mapper<Map<String, String>> {

	@Override
	public Map<String, String> fromString(final String json) throws MapException {
		if (json == null) {
			return null;
		}
		try {
			final JSONParser parser = new JSONParser();
			final Object object = parser.parse(json);
			if (object instanceof JSONObject) {
				final JSONObject jsonObject = (JSONObject) object;
				final Map<String, String> result = new HashMap<String, String>();

				@SuppressWarnings("unchecked")
				final Set<Object> iterator = jsonObject.keySet();
				for (final Object key : iterator) {
					final Object value = jsonObject.get(key);
					result.put(asString(key), asString(value));
				}
				return result;
			}
		}
		catch (final ParseException e) {
			throw new MapException("no valid json", e);
		}
		throw new MapException("no valid json");
	}

	private String asString(final Object key) {
		return key != null ? String.valueOf(key) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString(final Map<String, String> object) throws MapException {
		if (object == null) {
			return null;
		}
		final JSONObject jsonObject = new JSONObject();
		for (final Entry<String, String> e : object.entrySet()) {
			jsonObject.put(e.getKey(), e.getValue());
		}
		return jsonObject.toJSONString();
	}

}
