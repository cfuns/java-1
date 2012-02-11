package de.benjaminborbe.tools.mapper;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Provider;

public abstract class BaseMapper<T> implements Mapper<T> {

	private final Provider<T> provider;

	public BaseMapper(final Provider<T> provider) {
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
