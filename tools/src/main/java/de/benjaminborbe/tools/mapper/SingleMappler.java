package de.benjaminborbe.tools.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.inject.Provider;

public class SingleMappler<T> extends BaseMapper<T> {

	private final List<SingleMap<T>> mappings;

	public SingleMappler(final Provider<T> provider, final SingleMap<T>... mapSingles) {
		this(provider, Arrays.asList(mapSingles));
	}

	public SingleMappler(final Provider<T> provider, final Collection<SingleMap<T>> mapSingles) {
		super(provider);
		mappings = new ArrayList<SingleMap<T>>(mapSingles);
	}

	@Override
	public void map(final T object, final Map<String, String> data) throws MapException {
		for (final SingleMap<T> m : mappings) {
			data.put(m.getName(), m.map(object));
		}
	}

	@Override
	public void map(final Map<String, String> data, final T object) throws MapException {
		for (final SingleMap<T> m : mappings) {
			if (data.containsKey(m.getName())) {
				m.map(object, data.get(m.getName()));
			}
		}
	}
}
