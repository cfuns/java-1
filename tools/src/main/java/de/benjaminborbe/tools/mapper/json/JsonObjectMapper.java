package de.benjaminborbe.tools.mapper.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.inject.Provider;

import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;

public class JsonObjectMapper<T> {

	private final Provider<T> provider;

	private final List<StringObjectMapper<T>> mappings;

	@SafeVarargs
	public JsonObjectMapper(final Provider<T> provider, final StringObjectMapper<T>... mapSingles) {
		this(provider, Arrays.asList(mapSingles));
	}

	public JsonObjectMapper(final Provider<T> provider, final Collection<StringObjectMapper<T>> mapSingles) {
		this.provider = provider;
		this.mappings = new ArrayList<StringObjectMapper<T>>(mapSingles);
	}

	public String toJson(final T bean) throws MapException {
		final JSONObject jsonObject = new JSONObjectSimple();
		for (final StringObjectMapper<T> mapping : mappings) {
			final String value = mapping.map(bean);
			jsonObject.put(mapping.getName(), value);
		}

		try {
			final StringWriter sw = new StringWriter();
			jsonObject.writeJSONString(sw);
			return sw.toString();
		}
		catch (final IOException e) {
			throw new MapException(e);
		}
	}

	public T fromJson(final String json) throws MapException {
		try {
			final JSONParser parser = new JSONParserSimple();
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
		catch (final JSONParseException e) {
			throw new MapException(e);
		}
	}

	private String toString(final Object object) {
		return object != null ? String.valueOf(object) : null;
	}
}
