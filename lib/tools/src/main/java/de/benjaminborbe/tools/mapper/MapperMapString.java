package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapperMapString implements Mapper<Map<String, String>> {

	private final JSONParser jsonParser;

	@Inject
	public MapperMapString(final JSONParser jsonParser) {
		this.jsonParser = jsonParser;
	}

	@Override
	public Map<String, String> fromString(final String json) throws MapException {
		if (json == null) {
			return null;
		}
		try {
			final Object object = jsonParser.parse(json);
			if (object instanceof JSONObject) {
				final JSONObject jsonObject = (JSONObject) object;
				final Map<String, String> result = new HashMap<String, String>();

				for (final Entry<String, Object> e : jsonObject.entrySet()) {
					result.put(asString(e.getKey()), asString(e.getValue()));
				}
				return result;
			}
		} catch (final JSONParseException e) {
			throw new MapException("no valid json", e);
		}
		throw new MapException("no valid json");
	}

	private String asString(final Object key) {
		return key != null ? String.valueOf(key) : null;
	}

	@Override
	public String toString(final Map<String, String> object) throws MapException {
		if (object == null) {
			return null;
		}
		final JSONObject jsonObject = new JSONObjectSimple();
		for (final Entry<String, String> e : object.entrySet()) {
			jsonObject.put(e.getKey(), e.getValue());
		}
		try {
			final StringWriter sw = new StringWriter();
			jsonObject.writeJSONString(sw);
			return sw.toString();
		} catch (final IOException e) {
			throw new MapException(e);
		}
	}
}
