package de.benjaminborbe.tools.mapper.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.inject.Provider;

import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;

public class JsonObjectMapper<T> {

	private final Provider<T> provider;

	private final List<StringObjectMapper<T>> mappings;

	public JsonObjectMapper(final Provider<T> provider, final StringObjectMapper<T>... mapSingles) {
		this(provider, Arrays.asList(mapSingles));
	}

	public JsonObjectMapper(final Provider<T> provider, final Collection<StringObjectMapper<T>> mapSingles) {
		this.provider = provider;
		this.mappings = new ArrayList<StringObjectMapper<T>>(mapSingles);
	}

	@SuppressWarnings("unchecked")
	public String toJson(final T bean) throws MapException {
		final JSONObject jsonObject = new JSONObject();
		for (final StringObjectMapper<T> mapping : mappings) {
			final String value = mapping.map(bean);
			jsonObject.put(mapping.getName(), value);
		}
		return jsonObject.toJSONString();
	}

	public T fromJson(final String json) throws MapException {
		try {
			final JSONParser parser = new JSONParser();
			final Object object = parser.parse(json);
			if (object instanceof JSONObject) {
				final JSONObject jsonobject = (JSONObject) object;
				final T bean = provider.get();
				for (final StringObjectMapper<T> mapping : mappings) {
					if (jsonobject.containsKey(mapping.getName())) {
						final String value = toString(jsonobject.get(mapping.getName()));
						mapping.map(bean, value);
					}
				}
				return bean;
			}
			throw new MapException("not a json object");
		}
		catch (final ParseException e) {
			throw new MapException(e);
		}
	}

	private String toString(final Object object) {
		return object != null ? String.valueOf(object) : null;
	}
}
