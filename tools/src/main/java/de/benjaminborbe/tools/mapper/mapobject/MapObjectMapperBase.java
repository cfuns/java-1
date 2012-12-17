package de.benjaminborbe.tools.mapper.mapobject;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Provider;

import de.benjaminborbe.tools.mapper.MapException;

public abstract class MapObjectMapperBase<T> implements MapObjectMapper<T> {

	private final Provider<T> provider;

	public MapObjectMapperBase(final Provider<T> provider) {
		this.provider = provider;
	}

	@Override
	public Map<String, String> map(final T object) throws MapException {
		final Map<String, String> data = new HashMap<String, String>();
		map(object, data);
		return data;
	}

	@Override
	public T map(final Map<String, String> data) throws MapException {
		final T object = provider.get();
		map(data, object);
		return object;
	}

}
